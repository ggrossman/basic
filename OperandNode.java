class OperandNode extends ASTNode {
  private final Token token;

  public OperandNode(Token token) {
    this.token = token;
  }

  public Token getToken() { return token; }

  @Override
  public String toString() {
    if (token.getType() == TokenType.STRING) {
       return "\"" + token.getValue() + "\"";
    } else {
      return token.getValue();
    }
  }

  public Value evaluate(Context context) {
    if (token.getType() == TokenType.IDENTIFIER) {
      return context.getVariable(token.getValue());
    } else if (token.getType() == TokenType.NUMBER) {
      return new Value(Double.valueOf(token.getValue()));
    } else if (token.getType() == TokenType.STRING) {
      return new Value(token.getValue());
    } else {
      throw new IllegalArgumentException("Token type " + token.getType() + " cannot be evaluated");
    }
  }
}
