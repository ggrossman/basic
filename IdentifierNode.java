class IdentifierNode extends ASTNode {
  private final String identifier;

  public IdentifierNode(String identifier) {
    this.identifier = identifier;
  }

  public String getIdentifier() { return identifier; }

  @Override
  public String toString() {
    return identifier;
  }

  public Value evaluate(Context context) {
    return context.getVariable(identifier);
  }
}
