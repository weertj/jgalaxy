package org.jgalaxy.units;

import org.jgalaxy.Entity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.tech.JG_Tech;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

public class JG_Group extends Entity implements IJG_Group {

  static public final AtomicLong GROUPCOUNTER = new AtomicLong(1);

  static public IJG_Group of( Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Group group = of(id,name);
    group.setNumberOf( Integer.parseInt(XML_Utils.attr(pParent, "numberOf", "1" ) ));
    group.position().setX(Double.parseDouble(XML_Utils.attr(pParent, "x", "0" ) ));
    group.position().setY(Double.parseDouble(XML_Utils.attr(pParent, "y", "0" ) ));
    group.toPosition().setX(Double.parseDouble(XML_Utils.attr(pParent, "toX", "0" ) ));
    group.toPosition().setY(Double.parseDouble(XML_Utils.attr(pParent, "toY", "0" ) ));
    group.setUnitDesign(XML_Utils.attr(pParent, "unitDesign", "" ));
    group.setLoadType(XML_Utils.attr(pParent, "loadType", "" ));
    group.setLoad(Double.parseDouble(XML_Utils.attr(pParent, "load", "0" ) ));
    return group;
  }

  static public IJG_Group of( String pId, String pName ) {
    return new JG_Group( pId, pName );
  }

  private       int           mNumberOf;
  private       String        mFleet;
  private       String        mFaction;
  private final IJG_Position  mPosition = JG_Position.of(0,0);
  private final IJG_Position  mToPosition = JG_Position.of(0,0);
  private       String        mUnitDesign;
  private final IJG_Tech      mTech = JG_Tech.of();
  private       String        mLoadType;
  private       double        mLoad;
  private       String        mFrom;
  private       String        mTo;

  private JG_Group( String pId, String pName ) {
    super( pId, pName );
    return;
  }

  @Override
  public int numberOf() {
    return mNumberOf;
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
  public IJG_Position position() {
    return mPosition;
  }

  @Override
  public IJG_Position toPosition() {
    return mToPosition;
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
    return 0.0;
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
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element groupnode = pParent.getOwnerDocument().createElement( "group" );
    groupnode.setAttribute("id", id());
    groupnode.setAttribute("name", name());
    groupnode.setAttribute("numberOf", ""+mNumberOf );
    groupnode.setAttribute("x", ""+position().x());
    groupnode.setAttribute("y", ""+position().y());
    groupnode.setAttribute("toX", ""+toPosition().x());
    groupnode.setAttribute("toY", ""+toPosition().y());
    groupnode.setAttribute("unitDesign", mUnitDesign);
    if (mLoadType!=null) {
      groupnode.setAttribute( "loadType", mLoadType);
      groupnode.setAttribute("load", ""+mLoad);
    }
    pParent.appendChild(groupnode);
    return;
  }


}
