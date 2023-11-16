public class LoadNode extends ASTNode {
  private final ASTNode expression;

  public LoadNode(ASTNode expression) {
    this.expression = expression;
  }  
 
  public String toString() {
    return "LOAD " + expression;
  }

  public Value evaluate(Context context) {
    context.load(expression.evaluate(context).toString());
    return null;
  }
}