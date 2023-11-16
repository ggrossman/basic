class AssignmentNode extends ASTNode {
  private final OperandNode left;
  private final ASTNode right;

  public AssignmentNode(OperandNode left, ASTNode right) {
    this.left = left;
    this.right = right;
  }

  public String toString() {
    return left + " = " + right;
  }

  public Value evaluate(Context context) {
    String variableName = left.getToken().getValue();
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

