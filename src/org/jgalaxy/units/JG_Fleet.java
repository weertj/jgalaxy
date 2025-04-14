package org.jgalaxy.units;

import org.jgalaxy.Entity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JG_Fleet extends JG_Group implements IJG_Fleet {

  static public IJG_Fleet of(String pId, String pName, List<IJG_Group> pGroups ) {
    return new JG_Fleet(pId, pName, pGroups);
  }

  private final List<IJG_Group>  mGroups = new ArrayList<>(8);

  private JG_Fleet( String pId, String pName, List<IJG_Group> pGroups ) {
    super(pId, pName);
    mGroups.addAll(pGroups);
    return;
  }

  @Override
  public IJG_Position position() {
    if (mGroups.isEmpty()) {
      return null;
    }
    return mGroups.getFirst().position();
  }

  @Override
  public List<IJG_Group> groups() {
    return mGroups;
  }

  @Override
  public double maxSpeed(IJG_Faction pFaction) {
    return mGroups.stream().mapToDouble(g->g.maxSpeed(pFaction)).max().orElse(0.0);
  }

  @Override
  public String faction() {
    if (!mGroups.isEmpty()) {
      return mGroups.getFirst().faction();
    }
    return super.faction();
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element fleetnode = pParent.getOwnerDocument().createElement( "fleet" );
    fleetnode.setAttribute("id", id());
    fleetnode.setAttribute("name", name());
    if (!mGroups.isEmpty()) {
      fleetnode.setAttribute("x", "" + mGroups.getFirst().position().x());
      fleetnode.setAttribute("y", "" + mGroups.getFirst().position().y());
    }
    pParent.appendChild(fleetnode);
    return;
  }

  @Override
  public String toString() {
    return name();
  }
}
