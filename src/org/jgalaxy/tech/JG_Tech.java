package org.jgalaxy.tech;

import org.jgalaxy.utils.GEN_Math;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;

public class JG_Tech implements IJG_Tech {

  static public IJG_Tech of( Node pNode ) {
    IJG_Tech tech = of();
    tech.setDrive( Double.parseDouble(XML_Utils.attr(pNode,"tech.drive","1")) );
    tech.setWeapons( Double.parseDouble(XML_Utils.attr(pNode,"tech.weapons","1")) );
    tech.setShields( Double.parseDouble(XML_Utils.attr(pNode,"tech.shield","1")) );
    tech.setCargo( Double.parseDouble(XML_Utils.attr(pNode,"tech.cargo","1")) );
    return tech;
  }

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
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    JG_Tech jgTech = (JG_Tech) o;
    return Double.compare(mDrive, jgTech.mDrive) == 0 && Double.compare(mWeapons, jgTech.mWeapons) == 0 && Double.compare(mShields, jgTech.mShields) == 0 && Double.compare(mCargo, jgTech.mCargo) == 0;
  }

  @Override
  public int hashCode() {
    int result = Double.hashCode(mDrive);
    result = 31 * result + Double.hashCode(mWeapons);
    result = 31 * result + Double.hashCode(mShields);
    result = 31 * result + Double.hashCode(mCargo);
    return result;
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
    mDrive = GEN_Math.round01(drive);
    return;
  }

  @Override
  public void setWeapons(double weapons) {
    mWeapons = GEN_Math.round01(weapons);
    return;
  }

  @Override
  public void setShields(double shields) {
    mShields = GEN_Math.round01(shields);
    return;
  }

  @Override
  public void setCargo(double cargo) {
    mCargo = GEN_Math.round01(cargo);
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter) {
    ((Element)pParent).setAttribute("tech.drive", ""+drive());
    ((Element)pParent).setAttribute("tech.weapons", ""+weapons());
    ((Element)pParent).setAttribute("tech.shields", ""+shields());
    ((Element)pParent).setAttribute("tech.cargo", ""+cargo());
    return;
  }

}
