class AssignmentNode extends ASTNode {
  private final IdentifierNode left;
  private final ASTNode right;

  public AssignmentNode(IdentifierNode left, ASTNode right) {
    this.left = left;
    this.right = right;
  }

  public String toString() {
    return left + " = " + right;
  }

  public Value evaluate(Context context) {
    String variableName = left.getIdentifier();
    Value value = right.evaluate(context);
    if (value.isNumber() && variableName.endsWith("$")) {
      throw new IllegalArgumentException("Type mismatch error");
    }
    if (value.isString() && !variableName.endsWith("$")) {
      throw new IllegalArgumentException("Type mismatch error");
    }
    context.setVariable(variableName, value);
    return value;
  }
}

