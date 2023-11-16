public class GotoNode extends ASTNode {
  private final int lineNumber;

  public GotoNode(int lineNumber) {
    this.lineNumber = lineNumber;
 }

  public String toString() {
    return "GOTO " + lineNumber;
  }

  public int getLineNumber() { return lineNumber; }
 
  public Value evaluate(Context context) {
    context.gotoLine(lineNumber);
    return null;
  }
}