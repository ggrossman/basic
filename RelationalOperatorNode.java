class RelationalOperatorNode extends ASTNode {
  private final String operator;
  private final ASTNode left, right;

  public RelationalOperatorNode(String operator, ASTNode left, ASTNode right) {
    this.operator = operator;
    this.left = left;
    this.right = right;
  }

  public String toString() {
    return left + " " + operator + " " + right;
  }

  public Value evaluate(Context context) {
    Value leftValue = left.evaluate(context);
    Value rightValue = right.evaluate(context);

    if (leftValue.getType() != rightValue.getType()) {
      throw new IllegalArgumentException("Type mismatch error");
    }

    int compareResult = leftValue.compareTo(rightValue);
    boolean booleanResult;

    if (operator.equals("=")) {
        booleanResult = compareResult == 0;
    } else if (operator.equals("<>")) {
         booleanResult = compareResult != 0;
    } else if (operator.equals("<")) {
        booleanResult = compareResult < 0;
    } else if (operator.equals(">")) {
        booleanResult = compareResult > 0;
    } else if (operator.equals("<=")) {
        booleanResult = compareResult <= 0;
    } else if (operator.equals(">=")) {
        booleanResult = compareResult >= 0;
    } else {
      throw new IllegalArgumentException("Invalid relational operator " + operator);
    }

    return new Value(booleanResult ? 1.0 : 0.0);
  }
}
