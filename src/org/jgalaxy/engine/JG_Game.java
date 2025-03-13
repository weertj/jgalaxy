package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.jgalaxy.Galaxy;
import org.jgalaxy.IGalaxy;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.map.MAP_Map;
import org.jgalaxy.orders.IJG_Order;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.JG_Orders;
import org.jgalaxy.orders.SJG_OrderExecutor;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class JG_Game extends Entity implements IJG_Game {

  static public IJG_Game of(File pPath, long pTurnNumber ) throws IOException, ParserConfigurationException, SAXException {

    File gamexml = new File(pPath,"game_" + pTurnNumber + ".xml");

    String name;
    IGalaxy galaxy;
    try(FileInputStream fis = new FileInputStream(gamexml)) {
      String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
      Node root = XML_Utils.rootNodeBy(xml);
      Node gameNode = XML_Utils.childNodeByPath(root,"game").get();
      name = XML_Utils.attr(gameNode,"name");
      IMAP_Map map = MAP_Map.of( XML_Utils.childNodeByPath(gameNode, "map" ).get());
      galaxy = Galaxy.of(map);
    }

    IJG_Game game = new JG_Game(name,galaxy);
    game.setTurnNumber(pTurnNumber);

    File factions = new File( pPath, "factions");
    for( File nf : factions.listFiles() ) {
      File factionxml = new File(nf,"faction_" + pTurnNumber + ".xml");
      if (factionxml.exists()) {
        try (FileInputStream fis = new FileInputStream(factionxml)) {
          String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
          Node root = XML_Utils.rootNodeBy(xml);
          IJG_Faction faction = JG_Faction.of(game,XML_Utils.childNodeByPath(root, "faction").get());
          game.addFaction(faction);
        }
      }
      File orderxml = new File(nf,"orders_" + pTurnNumber + ".xml");
      if (orderxml.exists()) {
        try (FileInputStream fis = new FileInputStream(orderxml)) {
          String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
          Node root = XML_Utils.rootNodeBy(xml);
          IJG_Orders orders = JG_Orders.of( game.turnNumber(), XML_Utils.childNodeByPath(root, "orders").get());

        }
      }
    }

    File players = new File( pPath, "players");
    for( File nf : players.listFiles() ) {
      File playerxml = new File(nf,"player_" + pTurnNumber + ".xml");
      if (playerxml.exists()) {
        try (FileInputStream fis = new FileInputStream(playerxml)) {
          String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
          Node root = XML_Utils.rootNodeBy(xml);
          IJG_Player player = JG_Player.of(game,XML_Utils.childNodeByPath(root, "player").get());
          game.addPlayer(player);
        }
      }
    }

    return game;
  }

  private final IGalaxy           mGalaxy;
  private final List<IJG_Faction> mFactions = new ArrayList<>(8);
  private final List<IJG_Player>  mPlayers = new ArrayList<>(8);

  private       long mTurnNumber;

  private JG_Game( String pName, IGalaxy pGalaxy ) {
    super(pName,pName);
    mGalaxy = pGalaxy;
    return;
  }

  @Override
  public void setTurnNumber(long pNumber) {
    mTurnNumber = pNumber;
    return;
  }

  @Override
  public long turnNumber() {
    return mTurnNumber;
  }

  @Override
  public void addFaction(IJG_Faction pFaction) {
    mFactions.add(pFaction);
    return;
  }

  @Override
  public void addPlayer(IJG_Player pPlayer) {
    mPlayers.add(pPlayer);
    return;
  }

  @Override
  public List<IJG_Player> players() {
    return mPlayers;
  }

  @Override
  public List<IJG_Faction> factions() {
    return mFactions;
  }

  @Override
  public IGalaxy galaxy() {
    return mGalaxy;
  }

  @Override
  public void executeOrder(IJG_Order pOrder) {

    SJG_OrderExecutor.exec( pOrder,this );

    return;
  }

  @Override
  public void timeProgression(Duration pTimeStep) {
    mGalaxy.timeProgression(pTimeStep);


    for( IJG_Planet planet : mGalaxy.map().planets() ) {
      planet.timeProgression(pTimeStep);
    }


    mTurnNumber++;

    return;
  }


  /**
   * storeObject
   * @param pPath
   * @param pParent
   * @param pName
   */
  @Override
  public void storeObject(File pPath, Node pParent, String pName) {
    Document doc = XML_Utils.newXMLDocument();
    var root = doc.createElement("root");
    doc.appendChild(root);
    Element gamenode = doc.createElement( "game" );
    gamenode.setAttribute("name", name() );
    gamenode.setAttribute("turnNumber", ""+turnNumber() );
    try {
      mGalaxy.storeObject( null, gamenode, "" );
      for( var faction : factions() ) {
        faction.storeObject( new File( pPath, "factions" ), gamenode, ""  );
      }
      for( var player : players() ) {
        player.storeObject( new File( pPath, "players" ), gamenode, ""  );
      }
      root.appendChild(gamenode);
      String gamexml = XML_Utils.documentToString(doc);
      GEN_Streams.writeStringToFile(gamexml, new File(pPath, "game_" + mTurnNumber + ".xml"));
    } catch (IOException | TransformerException e ) {
      e.printStackTrace();
    }
    return;
  }


  @Override
  public String reportForPlayerAs(IJG_Player pPlayer, String pFormat) {
    String report = "";
    switch (pFormat) {
      case "plain": {
        report = reportPlain(pPlayer);
      }

    }
    return report;
  }


  private String reportPlain( IJG_Player pPlayer ) {
    String report = "";

    // ****
    report += "\t\t\tStatus of Players\n";
    report += "\n";
    report += "N\t\t\tD\tW\tS\tC\tP\tI\t#\tR\n";
    for( IJG_Faction faction : factions() ) {
      report += faction.name() + "\n";
    }
    report += "\n";

    // ****
    report += "\t\t\tYour Ship Types\n";
    report += "\n";
    report += "N\t\t\tD\tA\tW\tS\tC\tMass\tSpeed\tDef\n";
    report += "\n";

    List<IJG_Planet> planets = galaxy().map().planets();

    // ****
    report += "\t\t\tYour Planets\n";
    report += "\n";
    report += "N\t\t\tX\tY\tS\tP\tI\tR\tP\t$\tM\tC\tL\n";
    for( IJG_Planet planet : new ArrayList<>(planets) ) {
      if (pPlayer.planets().contains(planet)) {
        report += planet.name() + "\n";
        planets.remove(planet);
      }
    }
    report += "\n";

    // ****
    report += "\t\t\tUnidentified Planets\n";
    report += "\n";
    report += "N\t\t\tX\tY\n";
    for( IJG_Planet planet : new ArrayList<>(planets) ) {
      if (planet.population()>0) {
        report += planet.name() + "\n";
        planets.remove(planet);
      }
    }
    report += "\n";

    // ****
    report += "\t\t\tUninhabited Planets\n";
    report += "\n";
    report += "N\t\t\tX\tY\tS\tR\t$\tM\n";
    for( IJG_Planet planet : new ArrayList<>(planets) ) {
      report += planet.name() + "\t\t\t";
      report += planet.position().x() + "\t";
      report += planet.position().y() + "\t";
      report += "\n";
    }
    report += "\n";

    return report;
  }


}
