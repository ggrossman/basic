public class CommentNode extends ASTNode {
  private String comment;

  public CommentNode(String comment) {
    this.comment = comment;
  }

  public String toString() {
    return "REM " + comment;
 }

  public Value evaluate(Context context) {
    return null;
  }
}