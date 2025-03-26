package org.jgalaxy.units;

import org.jgalaxy.Entity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JG_Fleet extends Entity implements IJG_Fleet {

  static public IJG_Fleet of(String pId, String pName, List<IJG_Group> pGroups ) {
    return new JG_Fleet(pId, pName, pGroups);
  }

  private final IJG_Position     mPosition = JG_Position.of(0, 0);
  private final List<IJG_Group>  mGroups = new ArrayList<>(8);

  private JG_Fleet( String pId, String pName, List<IJG_Group> pGroups ) {
    super(pId, pName);
    mGroups.addAll(pGroups);
    if (mGroups.size() > 1) {
      mPosition.copyOf(mGroups.getFirst().position());
    }
    return;
  }

  @Override
  public IJG_Position position() {
    return mPosition;
  }

  @Override
  public List<IJG_Group> groups() {
    return mGroups;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element fleetnode = pParent.getOwnerDocument().createElement( "fleet" );
    fleetnode.setAttribute("id", id());
    fleetnode.setAttribute("name", name());
    fleetnode.setAttribute("x", ""+position().x());
    fleetnode.setAttribute("y", ""+position().y());
    pParent.appendChild(fleetnode);
    return;
  }


}
