package org.jgalaxy.units;

import org.jgalaxy.Entity;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Node;

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

  private final double mDrive;
  private final double mWeapons;
  private final int    mNrWeapons;
  private final double mShields;
  private final double mCargo;

  private JG_UnitDesign( String pId, String pName, double pDrive,double pWeapons,int pNrWeapons,double pShields,double pCargo) {
    super(pId,pName);
    mDrive = pDrive;
    mWeapons = pWeapons;
    mNrWeapons = pNrWeapons;
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
    return drive() + weapons()*0.5*(nrweapons()-1) + shields() + cargo();
  }

}
