package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.jgalaxy.units.IJG_UnitDesign;
import org.jgalaxy.units.JG_UnitDesign;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JG_Faction extends Entity implements IJG_Faction {

  static public IJG_Faction of(IJG_Game pGame, Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Faction faction = of(pGame,id,name);

    for(Element ud : XML_Utils.childElementsByName(pParent,"unitdesign")) {
      faction.addUnitDesign(JG_UnitDesign.of(ud));
    }

    return faction;
  }

  static public IJG_Faction of( IJG_Game pGame, String pID, String pName ) {
    IJG_Faction faction = new JG_Faction( pGame,pID,pName);
    return faction;
  }

  private final IJG_Game             mGame;
  private final List<IJG_UnitDesign> mUnitDesigns = new ArrayList<>(8);

  private JG_Faction( IJG_Game pGame, String pID, String pName ) {
    super(pID,pName);
    mGame = pGame;
    return;
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

  @Override
  public void storeObject(File pPath, Node pParent, String pName) {
    Document doc = XML_Utils.newXMLDocument();
    var root = doc.createElement("root");
    doc.appendChild(root);
    Element factionnode = doc.createElement( "faction" );
    factionnode.setAttribute("id", id() );
    factionnode.setAttribute("name", name() );
    root.appendChild(factionnode);
    try {
      File factiondir = new File(pPath,id());
      String factionxml = XML_Utils.documentToString(doc);
      GEN_Streams.writeStringToFile(factionxml, new File(factiondir, "faction_" + mGame.turnNumber() + ".xml"));
    } catch (IOException | TransformerException e ) {
      e.printStackTrace();
    }
  }

}
