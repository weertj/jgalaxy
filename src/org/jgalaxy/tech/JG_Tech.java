package org.jgalaxy.tech;

public class JG_Tech implements IJG_Tech {

  static public IJG_Tech of( double pDrive, double pWeapons, double pShields, double pCargo ) {
    return new JG_Tech( pDrive, pWeapons, pShields, pCargo );
  }

  private final double mDrive;
  private final double mWeapons;
  private final double mShields;
  private final double mCargo;

  private JG_Tech( double pDrive, double pWeapons, double pShields, double pCargo) {
    mDrive = pDrive;
    mWeapons = pWeapons;
    mShields = pShields;
    mCargo = pCargo;
    return;
  }

  @Override
  public double drive() {
    return mDrive;
  }

  @Override
  public double weapons() {
    return mWeapons;
  }

  @Override
  public double shields() {
    return mShields;
  }

  @Override
  public double cargo() {
    return mCargo;
  }
}
