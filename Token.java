class Token {
  private final TokenType type;
  private final String value;

  public Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  public TokenType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Token)) {
      return false;
    }
    Token other = (Token)o;
    if (type != other.type) {
      return false;
    }
    if (value == null || other.value == null) {
      return value == other.value;
    } else {
      return value.equals(other.value);
    }
 }

  @Override
  public String toString() {
    return String.format("%s: %s", type, value);
  }
}
