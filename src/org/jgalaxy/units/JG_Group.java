package org.jgalaxy.units;

import org.jgalaxy.Entity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.battle.B_Shot;
import org.jgalaxy.battle.IB_Shot;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.tech.JG_Tech;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JG_Group extends Entity implements IJG_Group {

  static public IJG_Group of( Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Group group = of(id,name);
    group.setNumberOf( Integer.parseInt(XML_Utils.attr(pParent, "nr", "1" ) ));
    group.position().setX(Double.parseDouble(XML_Utils.attr(pParent, "x", "0" ) ));
    group.position().setY(Double.parseDouble(XML_Utils.attr(pParent, "y", "0" ) ));
    group.toPosition().setX(Double.parseDouble(XML_Utils.attr(pParent, "toX", "0" ) ));
    group.toPosition().setY(Double.parseDouble(XML_Utils.attr(pParent, "toY", "0" ) ));
    group.lastStaticPosition().setX(Double.parseDouble(XML_Utils.attr(pParent, "lastStaticX", "0" ) ));
    group.lastStaticPosition().setY(Double.parseDouble(XML_Utils.attr(pParent, "lastStaticY", "0" ) ));
    group.setUnitDesign(XML_Utils.attr(pParent, "unitDesign", "" ));
    group.setLoadType(XML_Utils.attr(pParent, "loadType", null ));
    group.setLoad(Double.parseDouble(XML_Utils.attr(pParent, "load", "0" ) ));
    group.setFleet(XML_Utils.attr(pParent, "fleet", null ));
    group.tech().copyOf(JG_Tech.of(pParent));

    for( Node shotnode : XML_Utils.childElementsByName( pParent, "shot" )) {
      IB_Shot shot = new B_Shot(
        IB_Shot.TYPE.valueOf(XML_Utils.attr(shotnode,"type" )),
        Integer.parseInt(XML_Utils.attr(shotnode,"round" )),
        XML_Utils.attr(shotnode,"targetID" ),
        XML_Utils.attr(shotnode,"targetFaction" ),
        Integer.parseInt(XML_Utils.attr(shotnode,"hits" )));
      group.shotsMutable().add(shot);
    }

    return group;
  }

  static public IJG_Group of( String pId, String pName ) {
    return new JG_Group( pId, pName );
  }

  private       int           mNumberOf;
  private       String        mFleet;
  private       String        mFaction;
  private final IJG_Position  mLastStaticPosition = JG_Position.of(0,0);
  private final IJG_Position  mPosition = JG_Position.of(0,0);
  private final IJG_Position  mToPosition = JG_Position.of(0,0);
  private       String        mUnitDesign;
  private final IJG_Tech      mTech = JG_Tech.of();
  private       String        mLoadType;
  private       double        mLoad;
  private       String        mFrom;
  private       String        mTo;
  private final List<IB_Shot> mShots = new ArrayList<>(32);

  protected JG_Group( String pId, String pName ) {
    super( pId, pName );
    return;
  }

  @Override
  public void copyOf(IJG_Group pGroup) {
    mNumberOf = pGroup.getNumberOf();
    mFleet = pGroup.getFleet();
    mFaction = pGroup.faction();
    mLastStaticPosition.copyOf(pGroup.lastStaticPosition());
    mPosition.copyOf(pGroup.position());
    mToPosition.copyOf(pGroup.toPosition());
    mUnitDesign = pGroup.unitDesign();
    mTech.copyOf(pGroup.tech());
    mLoadType = pGroup.loadType();
    mLoad = pGroup.load();
    mFrom = pGroup.from();
    mTo = pGroup.to();
    return;
  }

  @Override
  public int getNumberOf() {
    return mNumberOf;
  }

  @Override
  public void setFleet(String pFleet) {
    mFleet = pFleet;
    return;
  }

  @Override
  public String getFleet() {
    return mFleet;
  }

  @Override
  public void setNumberOf(int pNumber) {
    mNumberOf = pNumber;
    return;
  }

  @Override
  public IJG_Tech tech() {
    return mTech;
  }

  @Override
  public String unitDesign() {
    return mUnitDesign;
  }

  @Override
  public void setUnitDesign(String unitDesign) {
    mUnitDesign = unitDesign;
    return;
  }

  @Override
  public IJG_Position lastStaticPosition() {
    return mLastStaticPosition;
  }

  @Override
  public IJG_Position position() {
    return mPosition;
  }

  @Override
  public IJG_Position toPosition() {
    return mToPosition;
  }

  @Override
  public double maxSpeed(IJG_Faction pFaction) {
    IJG_UnitDesign unitdesign = pFaction.getUnitDesignById(unitDesign());
    double speed = unitdesign.speed(tech(), totalCargoMass());
    return speed;
  }

  @Override
  public String from() {
    return mFrom;
  }

  @Override
  public void setFrom(String from) {
    mFrom = from;
    return;
  }

  @Override
  public String to() {
    return mTo;
  }

  @Override
  public void setTo(String to) {
    mTo = to;
    return;
  }

  @Override
  public String faction() {
    return mFaction;
  }

  @Override
  public void setFaction(String faction) {
    mFaction = faction;
    return;
  }

  @Override
  public double totalCargoMass() {
    if ("COL".equals(mLoadType) || "CAP".equals(mLoadType) || "MAT".equals(mLoadType)) {
      return mLoad;
    }
    return 0.0;
  }

  @Override
  public double totalMass( IJG_Faction pFaction ) {
    IJG_UnitDesign unitDesign = pFaction.getUnitDesignById(unitDesign());
    double mass = unitDesign.mass()*getNumberOf();
    return mass + totalCargoMass();
  }

  @Override
  public String loadType() {
    return mLoadType;
  }

  @Override
  public double load() {
    return mLoad;
  }

  @Override
  public void setLoadType(String pLoadType) {
    mLoadType = pLoadType;
    return;
  }

  @Override
  public void setLoad(double pLoad) {
    mLoad = pLoad;
    return;
  }

  @Override
  public IJG_Group breakOffGroup(IJG_Game pGame,IJG_Faction pThisFaction, int pNumberOf) {
    if (pNumberOf>0) {
      if (pNumberOf>=mNumberOf) {
        return this;
      }
      IJG_Group breakgroup;
      if (pThisFaction==null) {
        int newix = pGame.getFactionById(faction()).currentGroupCounterAndIncrement();
        breakgroup = new JG_Group(id() + "-" + newix, name() + "-" + newix);
      } else {
        int newix = pThisFaction.currentGroupCounterAndIncrement();
        breakgroup = new JG_Group(id() + "-" + newix, name() + "-" + newix);
      }
      breakgroup.copyOf(this);
      double ratio = (double)pNumberOf/(double)mNumberOf;
      if (mLoad>0) {
        mLoad *= ratio;
        breakgroup.setLoad(breakgroup.load()*(1-ratio));
      }
      mNumberOf -= pNumberOf;
      breakgroup.setNumberOf(pNumberOf);
      return breakgroup;
    }
    return null;
  }

  @Override
  public List<IB_Shot> shotsMutable() {
    return mShots;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element groupnode = pParent.getOwnerDocument().createElement( "group" );
    groupnode.setAttribute("id", id());
    groupnode.setAttribute("name", name());
    groupnode.setAttribute("nr", ""+mNumberOf );
    groupnode.setAttribute("x", ""+position().x());
    groupnode.setAttribute("y", ""+position().y());
    groupnode.setAttribute("toX", ""+toPosition().x());
    groupnode.setAttribute("toY", ""+toPosition().y());
    groupnode.setAttribute("lastStaticX", ""+lastStaticPosition().x());
    groupnode.setAttribute("lastStaticY", ""+lastStaticPosition().y());
    groupnode.setAttribute("unitDesign", mUnitDesign);
    if (mFleet!=null) {
      groupnode.setAttribute("fleet", mFleet);
    }
    if (mLoadType!=null && !mLoadType.isBlank()) {
      groupnode.setAttribute( "loadType", mLoadType);
      groupnode.setAttribute("load", ""+mLoad);
    }
    mTech.storeObject(null, groupnode, null, null);

    if (!pFilter.contains("shots") && !mShots.isEmpty()) {
      for( IB_Shot shot : mShots ) {
        Element shotnode = pParent.getOwnerDocument().createElement( "shot" );
        groupnode.appendChild(shotnode);
        shotnode.setAttribute("type", shot.type().name());
        shotnode.setAttribute("round", ""+shot.round());
        shotnode.setAttribute("targetID", shot.targetID());
        shotnode.setAttribute("targetFaction", shot.targetFaction());
        shotnode.setAttribute("hits", ""+shot.hits());
      }
    }

    pParent.appendChild(groupnode);
    return;
  }

  @Override
  public String toString() {
    return getNumberOf() + " x " + unitDesign();
  }
}
