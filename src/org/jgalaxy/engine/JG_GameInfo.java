package org.jgalaxy.engine;

import javafx.scene.image.Image;
import org.jgalaxy.Entity;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JG_GameInfo extends Entity implements IJG_GameInfo {

  static public IJG_GameInfo of(File pGameDir) throws Exception {
    return new JG_GameInfo(null,pGameDir.getName(), pGameDir,-1);
  }

  static public IJG_GameInfo of(Node pRoot) throws Exception {
    return new JG_GameInfo(pRoot,XML_Utils.attr(pRoot,"name"),null, Integer.parseInt(XML_Utils.attr(pRoot,"currentTurnNumber", "-1" )));
  }

  private final File    mGameDir;
  private       int     mCurrentTurnNumber = -1;
  private final List<String> mPlayers = new ArrayList<>();
  private final List<String> mFactions = new ArrayList<>();

  private JG_GameInfo( Node pRoot, String pName, File pDir, int pCurrentTurnNumber ) {
    super(pName,pName);
    mGameDir = pDir;
    mCurrentTurnNumber = pCurrentTurnNumber;
    if (pRoot!=null) {
      NodeList nl = pRoot.getChildNodes();
      for (int i = 0; i < nl.getLength(); i++) {
        Node n = nl.item(i);
        if (n.getNodeType() == Node.ELEMENT_NODE) {
          if (n.getNodeName().isBlank() || n.getNodeName().startsWith("player")) {
            mPlayers.add(n.getNodeName());
          } else if (n.getNodeName().isBlank() || n.getNodeName().startsWith("faction")) {
            mFactions.add(n.getNodeName());
          }
        }
      }
    }
    return;
  }

  @Override
  public File getGameDir() {
    return mGameDir;
  }

  @Override
  public void init() {
    mGameDir.mkdirs();
    return;
  }

  @Override
  public int firstTurnNumber() {
    int minTurnNumber = 0;
    for (File f : mGameDir.listFiles()) {
      if (f.getName().startsWith("game_")) {
        minTurnNumber = Math.min(minTurnNumber,
          Integer.parseInt(f.getName().substring("game_".length(), f.getName().indexOf('.'))));
      }
    }
    return minTurnNumber;
  }

  @Override
  public int currentTurnNumber() {
    if (mCurrentTurnNumber<0) {
      int maxTurnNumber = 0;
      for (File f : mGameDir.listFiles()) {
        if (f.getName().startsWith("game_")) {
          maxTurnNumber = Math.max(maxTurnNumber,
            Integer.parseInt(f.getName().substring("game_".length(), f.getName().indexOf('.'))));
        }
      }
      return maxTurnNumber;
    }
    return mCurrentTurnNumber;
  }

  @Override
  public List<String> factions() {
    return mFactions;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter) {
    Document doc = pParent.getOwnerDocument();
    Element gameinfonode = doc.createElement( "game" );
    gameinfonode.setAttribute("name", name() );
    gameinfonode.setAttribute("firstTurnNumber", ""+firstTurnNumber() );
    gameinfonode.setAttribute("currentTurnNumber", ""+currentTurnNumber() );
    // **** Players
    File players = new File(mGameDir,"players");
    for( File player : players.listFiles()) {
      if (player.getName().startsWith("player")) {
        gameinfonode.appendChild(doc.createElement(player.getName()));
      }
    }
    // **** Factions
    File factions = new File(mGameDir,"factions");
    for( File faction : factions.listFiles()) {
      if (faction.getName().startsWith("faction")) {
        gameinfonode.appendChild(doc.createElement(faction.getName()));
      }
    }
    pParent.appendChild(gameinfonode);
    return;
  }

}
