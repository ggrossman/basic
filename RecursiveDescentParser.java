import java.util.*;

public class RecursiveDescentParser {
  private Token lookahead;
  private Iterator<Token> tokens;

  public ASTNode parse(String expression) {
    LexicalScanner lexicalScanner = new LexicalScanner();
    tokens = lexicalScanner.tokenize(expression).iterator();
    lookahead = tokens.next();
    return statement();
  }

  private int lineNumber() {
    if (lookahead.getType() != TokenType.NUMBER) {
      return 0;
    }
    String s = lookahead.getValue();
    for (int i=0, n=s.length(); i<n; i++) {
      char ch = s.charAt(i);
      if (ch < '0' || ch > '9') {
        return 0;
      }
    }
    match();
    return Integer.valueOf(s);
  }

  private ASTNode statement() {
    int line = lineNumber();

    ASTNode result;
    if (lookahead.getType() == TokenType.REM) {
      match();
      if (lookahead.getType() != TokenType.STRING) {
        throw new RuntimeException("Comment expected");
      }
      result = new CommentNode(lookahead.getValue());
      match();
    } else if (lookahead.getType() == TokenType.PRINT) {
      match();
      result = new PrintNode(expression());
    } else if (lookahead.getType() == TokenType.INPUT) {
      match();
      result = new InputNode(identifier());
    } else if (lookahead.getType() == TokenType.RUN) {
      match();
      result = new RunNode();
    } else if (lookahead.getType() == TokenType.LOAD) {
      match();
      result = new LoadNode(expression());
    } else if (lookahead.getType() == TokenType.SAVE) {
      match();
      result = new SaveNode(expression());
    } else if (lookahead.getType() == TokenType.GOTO) {
      match();
      int gotoLine = lineNumber();
      if (gotoLine == 0) {
        throw new IllegalArgumentException("Line number expected after GOTO");
      }
      result = new GotoNode(gotoLine);
    } else if (lookahead.getType() == TokenType.IF) {
      match();
      ASTNode condition = expression();
      match(new Token(TokenType.THEN, null));
      ASTNode thenBranch = statement();
      result = new IfNode(condition, thenBranch);
   } else if (lookahead.getType() == TokenType.LIST) {
      match();
      result = new ListNode();
    } else if (lookahead.getType() == TokenType.IDENTIFIER || lookahead.getType() == TokenType.LET) {
      if (lookahead.getType() == TokenType.LET) {
        // LET is optional for variable declarations
        match();
      }
      IdentifierNode lvalue = identifier();
      if (tokenIsOperator("=")) {
        match();
        result = new AssignmentNode(lvalue, expression());
      } else {
        throw new IllegalArgumentException("Syntax error");
      }
    } else {
      throw new IllegalArgumentException("Syntax error");
    }

    if (line != 0) {
      return new LineNumberNode(line, result);
    } else {
      return result;
    }
  }

  private IdentifierNode identifier() {
    if (lookahead.getType() == TokenType.IDENTIFIER) {
      IdentifierNode result = new IdentifierNode(lookahead.getValue());
      match();
      return result;
    } else {
      throw new RuntimeException("Identifier expected");
    }
  }

  // expression -> relational ((EQUALS | NOT_EQUALS) relational)*
  private ASTNode expression() {
    ASTNode node = relational();
    while (tokenIsOperator("=") || tokenIsOperator("<>")) {
      String operator = lookahead.getValue();
      match();
      ASTNode rightNode = relational();
      node = new RelationalOperatorNode(operator, node, rightNode);
    }
    return node;
  }

  // relational -> term ((LESS_THAN | GREATER_THAN | LESS_THAN_OR_EQUAL | GREATER_THAN_OR_EQUAL) term)*
  private ASTNode relational() {
    ASTNode node = additive();
    while (tokenIsOperator("<") || tokenIsOperator(">") ||
          tokenIsOperator("<=") || tokenIsOperator(">=")) {
      String operator = lookahead.getValue();
      match();
      ASTNode rightNode = additive();
      node = new RelationalOperatorNode(operator, node, rightNode);
    }
    return node;
  }

  // additive -> term ((PLUS | MINUS) term)*
  private ASTNode additive() {
    ASTNode node = term();
    while (tokenIsOperator("+") || tokenIsOperator("-")) {
      String operator = lookahead.getValue();
      match();
      ASTNode rightNode = term();
      node = new ArithmeticOperatorNode(operator, node, rightNode);
    }
    return node;
  }

  // term -> unary ((MUL | DIV) unary)*
  private ASTNode term() {
    ASTNode node = unary();
    while (tokenIsOperator("*") || tokenIsOperator("/")) {
      String operator = lookahead.getValue();
      match();
      ASTNode rightNode = unary();
      node = new ArithmeticOperatorNode(operator, node, rightNode);
    }
    return node;
  }

  // unary -> MINUS factor | factor
  private ASTNode unary() {
    if (tokenIsOperator("-")) {
      match();
      ASTNode node = factor();
      return new UnaryMinusNode(node);
    } else {
      return factor();
    }
  }

  // factor -> NUMBER | IDENTIFIER | IDENTIFIER LPAREN expression (COMMA
  // expression)* RPAREN | LPAREN expression RPAREN
  private ASTNode factor() {
    ASTNode node;
    if (lookahead.getType() == TokenType.NUMBER) {
      node = new NumberLiteralNode(lookahead.getValue());
      match();
    } else if (lookahead.getType() == TokenType.STRING) {
      node = new StringLiteralNode(lookahead.getValue());
      match();
    } else if (lookahead.getType() == TokenType.IDENTIFIER) {
      String functionName = lookahead.getValue();
      match();
      if (tokenIsOperator("(")) {
        match();
        ArrayList<ASTNode> arguments = new ArrayList<>();
        if (!tokenIsOperator(")")) {
          arguments.add(expression());
          while (tokenIsOperator(",")) {
            match();
            arguments.add(expression());
          }
        }
        if (tokenIsOperator(")")) {
          match();
          node = new FunctionCallNode(functionName, arguments);
        } else {
          throw new RuntimeException("Expected )");
        }
      } else {
        node = new IdentifierNode(functionName);
      }
    } else if (tokenIsOperator("(")) {
      match();
      node = new ParenthesesNode(expression());
      if (tokenIsOperator(")")) {
        match();
      } else {
        throw new RuntimeException("Expected )");
      }
    } else {
      throw new RuntimeException("Unexpected token: " + lookahead);
    }
    return node;
  }

  private void match() {
    match(lookahead);
  }

  private void match(Token expected) {
    if (lookahead.equals(expected)) {
      if (tokens.hasNext()) {
        lookahead = tokens.next();
      } else {
        lookahead = new Token(null, ""); // End of input
      }
    } else {
      throw new RuntimeException("Unexpected token: " + lookahead);
    }
  }

  private boolean tokenIsOperator(String operator) {
    return lookahead.getType() == TokenType.OPERATOR && lookahead.getValue().equals(operator);
  }
}
