import java.util.ArrayList;

class FunctionCallNode extends ASTNode {
  private final String functionName;
  private final ArrayList<ASTNode> arguments;

  public FunctionCallNode(String functionName, ArrayList<ASTNode> arguments) {
    this.functionName = functionName;
    this.arguments = arguments;
  }

  public String toString() {
    String s = functionName + "(";
    for (int i = 0; i < arguments.size(); i++) {
      s += arguments.get(i);
      if (i < arguments.size() - 1) {
        s += ", ";
      }
    }
    s += ")";
    return s;
  }

  public Value evaluate(Context context) {
    if (functionName.equalsIgnoreCase("sqrt")) {
      if (arguments.size() < 1) {
        throw new IllegalArgumentException("sqrt requires 1 argument");
      }
      Value value = arguments.get(0).evaluate(context);
      if (value.isNumber()) {
       return new Value(Math.sqrt(value.getNumberValue()));
      } else {
        throw new IllegalArgumentException("Type mismatch");
      }
    } else if (functionName.equalsIgnoreCase("int")) {
      if (arguments.size() < 1) {
        throw new IllegalArgumentException("int requires 1 argument");
      }
      Value value = arguments.get(0).evaluate(context);
      if (value.isNumber()) {
       return new Value((int)value.getNumberValue());
      } else {
        throw new IllegalArgumentException("Type mismatch");
      }
    } else if (functionName.equalsIgnoreCase("rnd")) {
      return new Value(Math.random());
    } else {
      throw new IllegalArgumentException("Unknown function " + functionName);
    }
  }
}