package org.jgalaxy.engine;

import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JG_Games implements IJG_Games {

  static public final Map<String,IJG_Game> REALTIMEGAMES = new HashMap<>(16);

  static public IJG_Games of(File pGamesDir) throws Exception {
    return new JG_Games(pGamesDir);
  }

  private File mGamesDir;

  private JG_Games(File pGamesDir) {
    mGamesDir = pGamesDir;
    return;
  }

  @Override
  public List<IJG_GameInfo> games() {
    List<IJG_GameInfo> games = new ArrayList<>(8);
    for( File gdir : mGamesDir.listFiles()) {
      if (gdir.isDirectory()) {
        File gamecheck = new File(gdir,"game_0.xml");
        if (gamecheck.exists()) {
          try {
            games.add(JG_GameInfo.of(gdir));
          } catch (Exception e) {
          }
        }
      }
    }
    return games;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Document doc;
    Node root = pParent;
    if (pParent==null) {
      doc = XML_Utils.newXMLDocument();
      root = doc.createElement("root");
      doc.appendChild(root);
    } else {
      doc = pParent.getOwnerDocument();
    }
    Element gamesnode = doc.createElement( "games" );
    for( IJG_GameInfo gameInfo : games()) {
      gameInfo.storeObject(pPath,gamesnode,pName,pFilter);
    }
    root.appendChild(gamesnode);
    return;
  }


}
