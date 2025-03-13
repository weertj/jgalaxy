package org.jgalaxy.engine;

import org.jgalaxy.Galaxy;
import org.jgalaxy.IGalaxy;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.map.MAP_Map;
import org.jgalaxy.orders.IJG_Order;
import org.jgalaxy.orders.SJG_OrderExecutor;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class JG_Game implements IJG_Game {

  static public IJG_Game of(File pPath ) throws IOException, ParserConfigurationException, SAXException {

    File gamexml = new File(pPath,"game.xml");

    IGalaxy galaxy;
    try(FileInputStream fis = new FileInputStream(gamexml)) {
      String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
      Node root = XML_Utils.rootNodeBy(xml);
      Node gameNode = XML_Utils.childNodeByPath(root,"game").get();
      IMAP_Map map = MAP_Map.of( XML_Utils.childNodeByPath(gameNode, "map" ).get());
      galaxy = Galaxy.of(map);
    }

    IJG_Game game = new JG_Game(galaxy);

    File factions = new File( pPath, "factions");
    for( File nf : factions.listFiles() ) {
      File factionxml = new File(nf,"faction.xml");
      if (factionxml.exists()) {
        try (FileInputStream fis = new FileInputStream(factionxml)) {
          String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
          Node root = XML_Utils.rootNodeBy(xml);
          IJG_Faction faction = JG_Faction.of(XML_Utils.childNodeByPath(root, "faction").get());
          game.addFaction(faction);
        }
      }
    }

    File players = new File( pPath, "players");
    for( File nf : players.listFiles() ) {
      File playerxml = new File(nf,"player.xml");
      if (playerxml.exists()) {
        try (FileInputStream fis = new FileInputStream(playerxml)) {
          String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
          Node root = XML_Utils.rootNodeBy(xml);
          IJG_Player player = JG_Player.of(XML_Utils.childNodeByPath(root, "player").get());
          game.addPlayer(player);
        }
      }
    }

    return game;
  }

  private final IGalaxy mGalaxy;
  private final List<IJG_Faction> mFactions = new ArrayList<>(8);
  private final List<IJG_Player>  mPlayers = new ArrayList<>(8);

  private JG_Game( IGalaxy pGalaxy ) {
    mGalaxy = pGalaxy;
    return;
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

    // ****
    report += "\t\t\tYour Ship Types\n";
    report += "\n";
    report += "N\t\t\tD\tA\tW\tS\tC\tMass\tSpeed\tDef\n";
    for( IJG_Planet planet : galaxy().map().planets() ) {
      report += planet.name() + "\n";
    }

    // ****
    report += "\t\t\tYour Planets\n";
    report += "\n";
    report += "N\t\t\tX\tY\tS\tP\tI\tR\tP\t$\tM\tC\tL\n";
    for( IJG_Planet planet : galaxy().map().planets() ) {
      report += planet.name() + "\n";
    }

    return report;
  }


}
