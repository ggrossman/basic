import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class LexicalScanner {
  private static HashMap<String, TokenType> reservedWords = new HashMap<String, TokenType>();

  private List<Token> tokens;
  private StringBuilder buffer;
  private boolean remFound;

  static {
    reservedWords.put("print", TokenType.PRINT);
    reservedWords.put("goto", TokenType.GOTO);
    reservedWords.put("let", TokenType.LET);
    reservedWords.put("rem", TokenType.REM);
    reservedWords.put("list", TokenType.LIST);
    reservedWords.put("run", TokenType.RUN);
    reservedWords.put("load", TokenType.LOAD);
    reservedWords.put("save", TokenType.SAVE);
  }

  private void reset() {
    tokens = new ArrayList<>();
    buffer = new StringBuilder();
    remFound = false;
  }

  public List<Token> tokenize(String input) {
    reset();
    char[] chars = input.toCharArray();
    boolean inString = false;
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (inString) {
        if (c == '\"') {
          tokens.add(new Token(TokenType.STRING, buffer.toString()));
          buffer.setLength(0);
          inString = false;
        } else {
          buffer.append(c);
        }
      } else if (c == '\"') {
        flushBuffer();
        inString = true;
      } else if (Character.isWhitespace(c)) {
        flushBuffer();
      } else if (isOperator(c)) {
        flushBuffer();
        // Look ahead one character to check for two-character operators
        if (i + 1 < chars.length && isTwoCharOperator(c, chars[i + 1])) {
          tokens.add(new Token(TokenType.OPERATOR, Character.toString(c) + chars[i + 1]));
          i++; // Skip the next character
        } else {
          tokens.add(new Token(TokenType.OPERATOR, Character.toString(c)));
        }
      } else {
        buffer.append(c);
      }
      if (remFound) {
        // Stop scanning if a REM comment was found, and include all remaining
        // text as the comment.
        tokens.add(new Token(TokenType.STRING, input.substring(i).trim()));
        break;
      }
    }
    flushBuffer(); // flush buffer one more time at the end
    return tokens;
  }

  private void flushBuffer() {
    if (buffer.length() > 0) {
      String value = buffer.toString();
      TokenType tokenType = reservedWords.get(value.toLowerCase());
      if (tokenType != null) {
        tokens.add(new Token(tokenType, null));
        if (tokenType == TokenType.REM) {
          remFound = true;
        }
      } else {
        if (Character.isDigit(value.charAt(0))) {
          tokens.add(new Token(TokenType.NUMBER, value));
        } else {
          tokens.add(new Token(TokenType.IDENTIFIER, value));
        }
      }
      buffer.setLength(0);
    }
  }

  private static boolean isOperator(char c) {
    return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '=' || c == '<' || c == '>';
  }

  private static boolean isTwoCharOperator(char c1, char c2) {
    return (c1 == '<' && c2 == '=') || (c1 == '>' && c2 == '=') || (c1 == '<' && c2 == '>');
  }
}
