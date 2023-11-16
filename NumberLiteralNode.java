class NumberLiteralNode extends ASTNode {
  // We store numeric literals as strings so that they remain as the user typed them.
  private final String value;

  public NumberLiteralNode(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }

  public Value evaluate(Context context) {
    return new Value(Double.valueOf(value));
  }
}
