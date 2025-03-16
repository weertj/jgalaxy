package org.jgalaxy;

public class JG_Position implements IJG_Position {

  static public IJG_Position of(double x, double y) {
    return new JG_Position(x, y);
  }

  private double mX;
  private double mY;

  private JG_Position(double x, double y) {
    mX = x;
    mY = y;
    return;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    JG_Position that = (JG_Position) o;
    return Double.compare(mX, that.mX) == 0 && Double.compare(mY, that.mY) == 0;
  }

  @Override
  public int hashCode() {
    int result = Double.hashCode(mX);
    result = 31 * result + Double.hashCode(mY);
    return result;
  }

  @Override
  public double x() {
    return mX;
  }

  @Override
  public double y() {
    return mY;
  }

  @Override
  public void setX(double x) {
    mX = x;
    return;
  }

  @Override
  public void setY(double y) {
    mY = y;
    return;
  }

  @Override
  public String toString() {
    return "JG_Position{" +
      "mX=" + mX +
      ", mY=" + mY +
      '}';
  }
}
