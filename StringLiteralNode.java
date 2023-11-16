class StringLiteralNode extends ASTNode {
  private final String value;

  public StringLiteralNode(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "\"" + value + "\"";
  }

  public Value evaluate(Context context) {
    return new Value(value);
  }
}
