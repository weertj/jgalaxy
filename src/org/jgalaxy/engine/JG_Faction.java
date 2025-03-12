package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.w3c.dom.Node;

public class JG_Faction extends Entity implements IJG_Faction {

  static public IJG_Faction of(Node pParent ) {
    String id = pParent.getAttributes().getNamedItem("id").getNodeValue();
    String name = pParent.getAttributes().getNamedItem("name").getNodeValue();
    return new JG_Faction(id,name);
  }

  private JG_Faction( String pID, String pName ) {
    super(pID,pName);
  }

}
