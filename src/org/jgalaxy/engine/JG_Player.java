package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.jgalaxy.planets.IJG_Planet;
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

public class JG_Player extends Entity implements IJG_Player {

  static public IJG_Player of(IJG_Game pGame,Node pParent ) {
    String id = pParent.getAttributes().getNamedItem("id").getNodeValue();
    String name = pParent.getAttributes().getNamedItem("name").getNodeValue();
    return of(pGame,id,name);
  }

  static public IJG_Player of( IJG_Game pGame, String pID, String pName ) {
    return new JG_Player( pGame,pID,pName);
  }


  private final IJG_Game          mGame;
  private final List<IJG_Faction> mFactions = new ArrayList<>(8);
  private final List<IJG_Planet>  mPlanets = new ArrayList<>(8);

  private JG_Player( IJG_Game pGame, String pID, String pName ) {
    super(pID,pName);
    mGame = pGame;
  }

  @Override
  public void addFaction(IJG_Faction faction) {
    mFactions.add(faction);
    return;
  }

  @Override
  public List<IJG_Faction> factions() {
    return mFactions;
  }

  @Override
  public List<IJG_Planet> planets() {
    return mPlanets;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName) {
    Document doc = XML_Utils.newXMLDocument();
    var root = doc.createElement("root");
    doc.appendChild(root);
    Element playernode = doc.createElement( "player" );
    playernode.setAttribute("id", id() );
    playernode.setAttribute("name", name() );
    for( var faction : factions() ) {
      Element factionnode = doc.createElement( "faction" );
      factionnode.setAttribute("id", faction.id());
      playernode.appendChild(factionnode);
    }
    root.appendChild(playernode);
    try {
      File playerdir = new File(pPath,id());
      String playerxml = XML_Utils.documentToString(doc);
      GEN_Streams.writeStringToFile(playerxml, new File(playerdir, "player_" + mGame.turnNumber() + ".xml"));
    } catch (IOException | TransformerException e ) {
      e.printStackTrace();
    }
  }
}
