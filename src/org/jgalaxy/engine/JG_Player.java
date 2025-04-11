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
    String username = XML_Utils.attr(pParent,"username");
    String password = XML_Utils.attr(pParent, "password");
    IJG_Player player = of(pGame,username,password,id,name);
    for( Element factionnode : XML_Utils.childElementsByName(pParent,"faction")) {
      String factionid = XML_Utils.attr(factionnode, "id", "");
      IJG_Faction faction = pGame.getFactionById(factionid);
      if (faction==null) {
        player.addFaction(JG_Faction.of(pGame,XML_Utils.attr(factionnode,"id",null),XML_Utils.attr(factionnode,"name",null)));
      } else {
        player.addFaction(faction);
      }
    }
    return player;
  }

  static public IJG_Player of( IJG_Game pGame, String pUsername, String pPasswordEnc, String pID, String pName ) {
    return new JG_Player( pGame,pUsername,pPasswordEnc,pID,pName);
  }


  private final String            mUsername;
  private final String            mPasswordEnc;
  private final IJG_Game          mGame;
  private final List<IJG_Faction> mFactions = new ArrayList<>(8);

  private JG_Player( IJG_Game pGame, String pUsername, String pPasswordEnc, String pID, String pName ) {
    super(pID,pName);
    mUsername = pUsername;
    mPasswordEnc = pPasswordEnc;
    mGame = pGame;
    return;
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
  public String getUsername() {
    return mUsername;
  }

  @Override
  public String getPasswordEnc() {
    return mPasswordEnc;
  }

  @Override
  public IJG_Faction getFactionByID(String factionID) {
    return mFactions.stream().filter( f -> f.id().equals(factionID)).findFirst().orElse(null);
  }

  @Override
  public List<IJG_Planet> planets() {
    return mFactions.stream().flatMap(f -> f.planets().planets().stream()).toList();
  }

  @Override
  public void removeTurnNumber(File pPath, long pTurnNumber) {
    File playerdir = new File(pPath,id());
    File f = new File(playerdir, "player_" + pTurnNumber + ".xml");
    f.delete();
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Node root = pParent;
    Document doc;
    if (pPath==null) {
      doc = pParent.getOwnerDocument();
    } else {
      doc = XML_Utils.newXMLDocument();
      root = doc.createElement("root");
      doc.appendChild(root);
    }

//    mGame.storeObject(null,root,"",pFilter);
    Element playernode = doc.createElement( "player" );
    playernode.setAttribute("id", id() );
    playernode.setAttribute("name", name() );
    playernode.setAttribute("username", getUsername() );
    for( var faction : factions() ) {
      Element factionnode = doc.createElement( "faction" );
      factionnode.setAttribute("id", faction.id());
      factionnode.setAttribute("name", faction.name());
      double totpop = 0;
      double totind = 0;
      int nrplanets = 0;
      for( IJG_Planet planet : faction.planets().planetsOwnedBy(faction)) {
        totpop += planet.population();
        totind += planet.industry();
        nrplanets++;
      }
      factionnode.setAttribute("totalPop", "" + totpop );
      factionnode.setAttribute("totalIndustry", "" + totind );
      factionnode.setAttribute("tech", "" + faction.tech().totalTech() );
      factionnode.setAttribute("tech.drive", ""+faction.tech().drive());
      factionnode.setAttribute("tech.weapons", ""+faction.tech().weapons());
      factionnode.setAttribute("tech.shields", ""+faction.tech().shields());
      factionnode.setAttribute("tech.cargo", ""+faction.tech().cargo());
      factionnode.setAttribute("nrPlanets", "" + nrplanets );
      playernode.appendChild(factionnode);
    }
    root.appendChild(playernode);
    if (pPath!=null) {
      try {
        File playerdir = new File(pPath, id());
        playerdir.mkdirs();
        String playerxml = XML_Utils.documentToString(doc);
        GEN_Streams.writeStringToFile(playerxml, new File(playerdir, "player_" + mGame.turnNumber() + ".xml"));
      } catch (IOException | TransformerException e) {
        e.printStackTrace();
      }
    }
  }
}
