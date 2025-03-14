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

}
