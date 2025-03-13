package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class JG_Player extends Entity implements IJG_Player {

  static public IJG_Player of(Node pParent ) {
    String id = pParent.getAttributes().getNamedItem("id").getNodeValue();
    String name = pParent.getAttributes().getNamedItem("name").getNodeValue();
    return of(id,name);
  }

  static public IJG_Player of( String pID, String pName ) {
    return new JG_Player(pID,pName);
  }

  private final List<IJG_Faction> mFactions = new ArrayList<>(8);

  private JG_Player( String pID, String pName ) {
    super(pID,pName);
  }

  @Override
  public List<IJG_Faction> factions() {
    return mFactions;
  }
}
