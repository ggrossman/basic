import java.util.Scanner;

public class InputNode extends ASTNode {
  private final IdentifierNode identifier;

  public InputNode(IdentifierNode identifier) {
    this.identifier = identifier;
  }

  public String toString() {
    return "INPUT " + identifier;
  }

  public Value evaluate(Context context) {
    String name = identifier.getIdentifier();
    if (!name.endsWith("$")) {
      // Repeatedly ask for a number until we get a valid one
      while (true) {
        try {
          String input = context.getScanner().nextLine();
          double doubleValue = Double.valueOf(input);
          Value value = new Value(doubleValue);
          context.setVariable(name, value);
          return value;
        } catch (NumberFormatException e) {
          System.out.println("?REENTER");
        }
      }
    } else {    
      String input = context.getScanner().nextLine();
      Value value = new Value(input);
      context.setVariable(name, value);
      return value;
    }
  }
}