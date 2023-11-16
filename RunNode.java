public class RunNode extends ASTNode {
  public String toString() {
    return "RUN";
 }

  public Value evaluate(Context context) {
    context.run();
    return null;
  }
}