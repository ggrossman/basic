public class Value implements Comparable<Value> {
  private final ValueType type;
  private final String stringValue;
  private final double numberValue;

  public Value(String stringValue) {
    this.stringValue = stringValue;
    this.numberValue = Double.NaN;
    this.type = ValueType.STRING;
  }

  public Value(double numberValue) {
    this.numberValue = numberValue;
    this.stringValue = null;
    this.type = ValueType.NUMBER;
  }

  public ValueType getType() { return type; }
  public boolean isNumber() { return type == ValueType.NUMBER; }
  public boolean isString() { return type == ValueType.STRING; }

  public double getNumberValue() {
    if (type != ValueType.NUMBER) {
      throw new IllegalArgumentException("Value must be of type ValueType.NUMBER");
    }
    return numberValue;
  }

  public String getStringValue() {
    if (type != ValueType.STRING) {
      throw new IllegalArgumentException("Value must be of type ValueType.STRING");
    }
    return stringValue;
  }

  public boolean isTruthy() {
    if (type == ValueType.NUMBER) {
      return numberValue != 0.0;
    } else {
      return stringValue.length() > 0;
    }
  }
 
  @Override
  public int compareTo(Value other) {
    if (this.type != other.type) {
      return -1;
    } else if (this.type == ValueType.NUMBER) {
      return Double.compare(this.numberValue, other.numberValue);     
    } else {
      return stringValue.compareTo(other.stringValue);
    }
  }
 
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Value)) {
      return false;
    }
    Value other = (Value)o;
    if (type != other.type) {
      return false;
    }
    if (type == ValueType.STRING) {
      return stringValue.equals(other.stringValue);
    } else {
      return numberValue == other.numberValue;
    }
  }

  public String toString() {
    if (type == ValueType.NUMBER) {
      String s = Double.toString(numberValue);
      // Trim off ".0" at end
      if (s.endsWith(".0")) {
        s = s.substring(0, s.length()-2);
      }
      return s;
    } else {
      return stringValue;
    }
  }
}