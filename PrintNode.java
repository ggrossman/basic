public class PrintNode extends ASTNode {
  private final ASTNode expression;

  public PrintNode(ASTNode expression) {
    this.expression = expression;
  }

  public String toString() {
    return "PRINT " + expression;
  }

  public Value evaluate(Context context) {
    Value value = expression.evaluate(context);
    System.out.println(value);
    return value;
  }
}