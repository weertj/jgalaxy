package org.jgalaxy.tech;

public class JG_Tech implements IJG_Tech {

  static public IJG_Tech of() {
    return of(1.0,1.0,1.0,1.0);
  }

  static public IJG_Tech of( double pDrive, double pWeapons, double pShields, double pCargo ) {
    return new JG_Tech( pDrive, pWeapons, pShields, pCargo );
  }

  private double mDrive;
  private double mWeapons;
  private double mShields;
  private double mCargo;

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

  @Override
  public void setDrive(double drive) {
    mDrive = drive;
    return;
  }

  @Override
  public void setWeapons(double weapons) {
    mWeapons = weapons;
    return;
  }

  @Override
  public void setShields(double shields) {
    mShields = shields;
    return;
  }

  @Override
  public void setCargo(double cargo) {
    mCargo = cargo;
    return;
  }
}
