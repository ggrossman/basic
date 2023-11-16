public class ParenthesesNode extends ASTNode {
  private final ASTNode expression;

  public ParenthesesNode(ASTNode expression) {
    this.expression = expression;
  }

  public String toString() {
    return "(" + expression + ")";
  }

  public Value evaluate(Context context) {
    return expression.evaluate(context);
  }
}