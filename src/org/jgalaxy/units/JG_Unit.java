package org.jgalaxy.units;

public class JG_Unit implements IJG_Unit {

  static public IJG_Unit of( IJG_UnitDesign pDesign ) {
    return new JG_Unit(pDesign);
  }

  private       int            mNumberOf = 0;
  private final IJG_UnitDesign mDesign;

  private JG_Unit( IJG_UnitDesign pDesign ) {
    mDesign = pDesign;
    return;
  }


  @Override
  public int numberOf() {
    return mNumberOf;
  }

  @Override
  public IJG_UnitDesign design() {
    return mDesign;
  }

  @Override
  public void setNumberOf(int pNumber) {
    mNumberOf = pNumber;
    return;
  }
}
