public class LineNumberNode extends ASTNode {
  private final int lineNumber;
  private final ASTNode statement;

  public LineNumberNode(int lineNumber, ASTNode statement) {
    this.lineNumber = lineNumber;
    this.statement = statement; 
  }

  public String toString() {
    return lineNumber + " " + statement;
  }

  public int getLineNumber() { return lineNumber; }
  public ASTNode getStatement() { return statement; }

  public Value evaluate(Context context) {
    context.addLine(this);
    return null;
  }
}