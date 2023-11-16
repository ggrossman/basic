class ArithmeticOperatorNode extends ASTNode {
  private final String operator;
  private final ASTNode left, right;

  public ArithmeticOperatorNode(String operator, ASTNode left, ASTNode right) {
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

    if (operator.equals("+")) {
      if (leftValue.isString() || rightValue.isString()) {
        return new Value(leftValue.toString() + rightValue.toString());
      } else {
        return new Value(leftValue.getNumberValue() + rightValue.getNumberValue());
      }
    } else if (operator.equals("-")) {
      if (!leftValue.isNumber() || !rightValue.isNumber()) {
        throw new IllegalArgumentException("Both arguments to - operator must be numeric");
      }
      return new Value(leftValue.getNumberValue() - rightValue.getNumberValue());
    } else if (operator.equals("*")) {
      if (!leftValue.isNumber() || !rightValue.isNumber()) {
        throw new IllegalArgumentException("Both arguments to * operator must be numeric");
      }
      return new Value(leftValue.getNumberValue() * rightValue.getNumberValue());
    } else if (operator.equals("/")) {
      if (!leftValue.isNumber() || !rightValue.isNumber()) {
        throw new IllegalArgumentException("Both arguments to / operator must be numeric");
      }
      return new Value(leftValue.getNumberValue() / rightValue.getNumberValue());
    } else {
      throw new IllegalArgumentException("Illegal operator " + operator);
    }
  }
}
