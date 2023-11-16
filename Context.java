import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Context {
  private HashMap<String, Value> variables;
  private ArrayList<LineNumberNode> program;
  private Scanner scanner;
  private Iterator<LineNumberNode> programCounter;
  private RecursiveDescentParser parser;

  public Context(Scanner scanner, RecursiveDescentParser parser) {
    variables = new HashMap<String, Value>();
    program = new ArrayList<LineNumberNode>();
    this.scanner = scanner;
    programCounter = null;
    this.parser = parser;
  }

  public void list() {
    for (LineNumberNode lineNumberNode : program) {
      System.out.println(lineNumberNode);
    }
  }

  public void newProgram() {
    programCounter = null;
    program.clear();
    variables.clear();
  }

  public void save(String path) {
    File file = new File(path);
    BufferedWriter bufferedWriter = null;

    try {
      bufferedWriter = new BufferedWriter(new FileWriter(file));
      for (LineNumberNode lineNumberNode : program) {
        bufferedWriter.write(lineNumberNode.toString());
        bufferedWriter.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (bufferedWriter != null) {
          bufferedWriter.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public void load(String path) {
    File file = new File(path);
    BufferedReader bufferedReader = null;
    try {
      bufferedReader = new BufferedReader(new FileReader(file));
      newProgram();
      String line;    
      while ((line = bufferedReader.readLine()) != null) {
        ASTNode ast = parser.parse(line);
        ast.evaluate(this);
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

  public void gotoLine(int lineNumber) {
    LineNumberNode lineNumberNode = new LineNumberNode(lineNumber, null);
    Comparator<LineNumberNode> comparator = Comparator.comparingInt(LineNumberNode::getLineNumber);
    int index = Collections.binarySearch(program, lineNumberNode, comparator);

    if (index < 0) {
      throw new IllegalArgumentException("Line " + lineNumber + " not found");
    } else {
      boolean wasRunning = programCounter != null;
      programCounter = program.listIterator(index);
      if (!wasRunning) {
        runLoop();
      }
    }
  }

  public void runLoop() {
    while (programCounter.hasNext()) {
      LineNumberNode lineNumberNode = programCounter.next();
      lineNumberNode.getStatement().evaluate(this);
    }
    programCounter = null;
  }

  public void run() {
    if (programCounter != null) {
      // Restart the program if RUN called reentrantly
      programCounter = program.iterator();
    } else {
      programCounter = program.iterator();
      runLoop();
    }
  }

  public void addLine(LineNumberNode lineNumberNode) {
    Comparator<LineNumberNode> comparator = Comparator.comparingInt(LineNumberNode::getLineNumber);
    int index = Collections.binarySearch(program, lineNumberNode, comparator);

    if (index < 0) {
      program.add(-index - 1, lineNumberNode); // element not found, so add it at the appropriate position
    } else {
      program.set(index, lineNumberNode); // element found, so replace it
    }
  }

  public Value getVariable(String name) {
    name = name.toLowerCase();
    Value value = variables.get(name);
    if (value == null) {
      // Undefined variables get the default value for their type
      if (name.endsWith("$")) {
        return new Value("");
      } else {
        return new Value(0.0);
      }
    }
    return value;
  }

  public void setVariable(String name, Value value) {
    name = name.toLowerCase();
    variables.put(name, value);
  }

  public Scanner getScanner() {
    return scanner;
  }
}
