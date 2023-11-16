public class SaveNode extends ASTNode {
  private final ASTNode expression;

  public SaveNode(ASTNode expression) {
    this.expression = expression;
  }

  public String toString() {
    return "SAVE " + expression;
  }

  public Value evaluate(Context context) {
    context.save(expression.evaluate(context).toString());
    return null;
  }
}