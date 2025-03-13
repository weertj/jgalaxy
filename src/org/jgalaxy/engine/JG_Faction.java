package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.jgalaxy.units.IJG_UnitDesign;
import org.jgalaxy.units.JG_UnitDesign;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class JG_Faction extends Entity implements IJG_Faction {

  static public IJG_Faction of(Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Faction faction = of(id,name);

    for(Element ud : XML_Utils.childElementsByName(pParent,"unitdesign")) {
      faction.addUnitDesign(JG_UnitDesign.of(ud));
    }

    return faction;
  }

  static public IJG_Faction of( String pID, String pName ) {
    IJG_Faction faction = new JG_Faction(pID,pName);
    return faction;
  }

  private final List<IJG_UnitDesign> mUnitDesigns = new ArrayList<>(8);

  private JG_Faction( String pID, String pName ) {
    super(pID,pName);
  }

  @Override
  public void addUnitDesign(IJG_UnitDesign pDesign) {
    mUnitDesigns.add(pDesign);
    return;
  }

  @Override
  public List<IJG_UnitDesign> unitDesigns() {
    return mUnitDesigns;
  }
}
