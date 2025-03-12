package org.jgalaxy.units;

import org.jgalaxy.tech.IJG_Tech;

public class JG_UnitDesign implements IJG_UnitDesign {

  static public IJG_UnitDesign of( double pDrive,double pWeapons,int pNrWeapons,double pShields,double pCargo, IJG_Tech pTech ) {
    return new JG_UnitDesign(pDrive,pWeapons,pNrWeapons,pShields,pCargo,pTech);
  }

  private final double mDrive;
  private final double mWeapons;
  private final int    mNrWeapons;
  private final double mShields;
  private final double mCargo;
  private final IJG_Tech mTech;

  private JG_UnitDesign( double pDrive,double pWeapons,int pNrWeapons,double pShields,double pCargo, IJG_Tech pTech) {
    mDrive = pDrive;
    mWeapons = pWeapons;
    mNrWeapons = pNrWeapons;
    mShields = pShields;
    mCargo = pCargo;
    mTech = pTech;
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
  public int nrweapons() {
    return mNrWeapons;
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
  public IJG_Tech tech() {
    return mTech;
  }

  @Override
  public double mass() {
    return drive() + weapons()*0.5*(nrweapons()-1) + shields() + cargo();
  }

}
