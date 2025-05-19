package org.jgalaxy.units;

import org.jgalaxy.Entity;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.utils.GEN_Math;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.Objects;

public class JG_UnitDesign extends Entity implements IJG_UnitDesign {

  static public IJG_UnitDesign of( Node pParent ) {
    return of(
      XML_Utils.attr(pParent,"id"),
      XML_Utils.attr(pParent,"name"),
      Double.parseDouble(XML_Utils.attr(pParent,"drive")),
      Double.parseDouble(XML_Utils.attr(pParent,"weapons")),
      Integer.parseInt(XML_Utils.attr(pParent,"nrweapons")),
      Double.parseDouble(XML_Utils.attr(pParent,"shields")),
      Double.parseDouble(XML_Utils.attr(pParent,"cargo"))
    );
  }

  static public IJG_UnitDesign of( String pId, String pName, double pDrive,double pWeapons,int pNrWeapons,double pShields,double pCargo ) {
    return new JG_UnitDesign( pId, pName, pDrive,pWeapons,pNrWeapons,pShields,pCargo);
  }

  static public double killChance( double pWeapon, double pShields ) {
    return ((Math.log(pWeapon/pShields)/Math.log(4))+1)/2;
  }

  static public double shieldsImmuneForWeapons( double pShields ) {
    return pShields/4.0;
  }


  private final double mDrive;
  private final double mWeapons;
  private final int    mNrWeapons;
  private final double mShields;
  private final double mCargo;

  private JG_UnitDesign( String pId, String pName, double pDrive,double pWeapons,int pNrWeapons,double pShields,double pCargo) {
    super(pId,pName);
    mDrive = GEN_Math.round02(pDrive);
    mWeapons = GEN_Math.round02(pWeapons);
    mNrWeapons = pNrWeapons;
    mShields = GEN_Math.round02(pShields);
    mCargo = GEN_Math.round02(pCargo);
    return;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    JG_UnitDesign that = (JG_UnitDesign) o;
    return Objects.equals(name(),that.name()) &&
      Double.compare(mDrive, that.mDrive) == 0 &&
      Double.compare(mWeapons, that.mWeapons) == 0 &&
      mNrWeapons == that.mNrWeapons &&
      Double.compare(mShields, that.mShields) == 0 &&
      Double.compare(mCargo, that.mCargo) == 0;
  }

  @Override
  public int hashCode() {
    int result = 0;
    result = 31 * result + name().hashCode();
    result = 31 * result + Double.hashCode(mDrive);
    result = 31 * result + Double.hashCode(mWeapons);
    result = 31 * result + mNrWeapons;
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
  public double mass() {
    double weapons = weapons() + weapons()*0.5*(nrweapons()-1);
    if (nrweapons()<=0) {
      weapons = 0;
    }
    return drive() + weapons + shields() + cargo();
  }

  @Override
  public double speed(IJG_Tech pTech, double pCargoMass) {
//    return drive() + pTech.drive() * 20/mass();
    return 20*pTech.drive() * (drive()/(mass()+pCargoMass));
  }

  @Override
  public double canCarry(IJG_Tech pTech) {
    return pTech.cargo() * (cargo() + (cargo()*cargo())/10);
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element unitdesign = pParent.getOwnerDocument().createElement("unitdesign");
    unitdesign.setAttribute("id", id() );
    unitdesign.setAttribute("name", name() );
    unitdesign.setAttribute("drive", ""+drive() );
    unitdesign.setAttribute("weapons", ""+weapons() );
    unitdesign.setAttribute("nrweapons", ""+nrweapons() );
    unitdesign.setAttribute("shields", ""+shields() );
    unitdesign.setAttribute("cargo", ""+cargo() );
    pParent.appendChild(unitdesign);
    return;
  }

  @Override
  public String toString() {
    return name();
  }
}
