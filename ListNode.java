public class ListNode extends ASTNode {
  public String toString() {
    return "LIST";
  }

  public Value evaluate(Context context) {
    context.list();
    return null;
  }
}