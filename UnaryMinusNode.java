public class UnaryMinusNode extends ASTNode {
  private final ASTNode operand;

  public UnaryMinusNode(ASTNode operand) {
    this.operand = operand;
  }

  public String toString() {
    return "-" + operand;
  }

  public Value evaluate(Context context) {
    Value operandValue = operand.evaluate(context);
 
    if (!operandValue.isNumber()) {
      throw new IllegalArgumentException("Unary - operand must be numeric");
    }

    return new Value(-operandValue.getNumberValue());
  }
}