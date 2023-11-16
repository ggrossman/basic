import java.util.Scanner;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

public class BasicInterpreter {
  public void repl() {
    System.out.println("Welcome to El Camino BASIC!");
    System.out.println("Type help for a list of commands.");
    System.out.println();
    RecursiveDescentParser parser = new RecursiveDescentParser();
    Scanner scanner = new Scanner(System.in);
    Context context = new Context(scanner, parser);
    while (true) {
      System.out.print("]");
      String input = System.console().readLine();
      if (input.equalsIgnoreCase("quit")) {
        break;
      } else if (input.equalsIgnoreCase("help")) {
        help();
      } else {
        try {
          ASTNode ast = parser.parse(input);
          ast.evaluate(context);
        } catch (Exception e) {
          System.out.println(e.getMessage());
        }
      }
    }
    scanner.close();
  }

  private void help() {
    File file = new File("help.txt");
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(file));
      String line;    
      while ((line = bufferedReader.readLine()) != null) {
        System.out.println(line);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
