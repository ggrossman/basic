public class IfNode extends ASTNode {
  private final ASTNode expression;
  private final ASTNode statement;

  public IfNode(ASTNode expression, ASTNode statement) {
    this.expression = expression;
    this.statement = statement;
  }

  public String toString() {
    return "IF " + expression + " THEN " + statement;  
  }

  public Value evaluate(Context context) {
    Value value = expression.evaluate(context);
    if (value.isTruthy()) {
      return statement.evaluate(context);
    } else {
      return null;
    }
  }

}