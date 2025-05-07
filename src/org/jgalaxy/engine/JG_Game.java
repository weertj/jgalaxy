package org.jgalaxy.engine;

import org.jgalaxy.*;
import org.jgalaxy.ai.AI_GameMasterFaction;
import org.jgalaxy.ai.IAI_Faction;
import org.jgalaxy.battle.SB_Battle;
import org.jgalaxy.los.FLOS_Visibility;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.map.MAP_Map;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.JG_Orders;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_Groups;
import org.jgalaxy.units.JG_Groups;
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
import java.util.*;
import java.util.stream.Collectors;

public class JG_Game extends Entity implements IJG_Game {


  static public IJG_Game of(File pPath, Node pParent, long pTurnNumber ) throws IOException, ParserConfigurationException, SAXException {

    Node root = pParent;
    String name;
    IGalaxy galaxy;

    if (root==null) {
      File gamexml = new File(pPath, "game_" + pTurnNumber + ".xml");
      try (FileInputStream fis = new FileInputStream(gamexml)) {
        String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
        root = XML_Utils.rootNodeBy(xml);
      }
    }
    Node gameNode = XML_Utils.childNodeByPath(root,"game").get();
    name = XML_Utils.attr(gameNode,"name");
    IMAP_Map map = MAP_Map.of( null, XML_Utils.childNodeByPath(gameNode, "map" ).get());
    galaxy = Galaxy.of(map);

    IJG_Game game = of(name,galaxy);
    game.setTurnNumber(pTurnNumber);
    game.setGameType(XML_Utils.attr(gameNode,"gameType",""));
    game.setTurnIntervalSecs( Long.parseLong(XML_Utils.attr(gameNode, "turnIntervalSecs", "-1" )));
    game.setTurnHistory( Long.parseLong(XML_Utils.attr(gameNode, "turnHistory", "-1" )));
    game.setRealTime(Boolean.valueOf(XML_Utils.attr(gameNode, "realtime", "false" )));
    game.setNextRun(XML_Utils.attr(gameNode, "nextRun"));
    game.setTimeProgressionDays(Double.parseDouble(XML_Utils.attr(gameNode, "timeProgressionDays", "365" )));
    game.setRunWhenAllOrdersAreIn(Boolean.valueOf(XML_Utils.attr(gameNode, "runWhenAllOrdersAreIn", "false" )));
    game.setUTCTime(Long.parseLong(XML_Utils.attr(gameNode, "gameTime", "0" )));

    if (pPath==null) {
//      for( Element factionNode : XML_Utils.childElementsByName(root,"faction")) {
//        game.addFaction( JG_Faction.of(game, factionNode), JG_Faction.of(game, factionNode));
//      }
    } else {
      File factions = new File(pPath, "factions");
      for (File nf : factions.listFiles()) {
        File factionxml = new File(nf, "faction_" + pTurnNumber + ".xml");
        if (factionxml.exists()) {
          try (FileInputStream fis = new FileInputStream(factionxml)) {
            String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
            root = XML_Utils.rootNodeBy(xml);
            IJG_Faction origfaction = JG_Faction.of(game, XML_Utils.childNodeByPath(root, "faction").get());
            IJG_Faction faction = JG_Faction.of(game, XML_Utils.childNodeByPath(root, "faction").get());
            game.addFaction(origfaction,faction);
            File orderxml = new File(nf, "orders_" + pTurnNumber + ".xml");
            if (orderxml.exists()) {
              try (FileInputStream ofis = new FileInputStream(orderxml)) {
                String oxml = GEN_Streams.readAsString(ofis, Charset.defaultCharset());
                Node oroot = XML_Utils.rootNodeBy(oxml);
                IJG_Orders orders = JG_Orders.of(game.turnNumber(), XML_Utils.childNodeByPath(oroot, "orders").get());
                faction.setOrders(orders);
              }
            }
          }
        }
      }
    }

    if (pPath==null) {
      var oplayers = XML_Utils.childNodeByPath(gameNode,"players");
      if (oplayers.isPresent()) {
        for( Node playernode : XML_Utils.childElementsByName(oplayers.get(), "player" )) {
          IJG_Player player = JG_Player.of(game,playernode);
          game.addPlayer(player);
        }
      }
    } else {
      File players = new File(pPath, "players");
      for (File nf : players.listFiles()) {
        File playerxml = new File(nf, "player_" + pTurnNumber + ".xml");
        if (playerxml.exists()) {
          try (FileInputStream fis = new FileInputStream(playerxml)) {
            String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
            root = XML_Utils.rootNodeBy(xml);
            IJG_Player player = JG_Player.of(game, XML_Utils.childNodeByPath(root, "player").get());
            game.addPlayer(player);
          }
        }
      }
    }

    return game;
  }

  static public IJG_Game of( String pName, IGalaxy pGalaxy ) {
    return new JG_Game(pName,pGalaxy);
  }

  private final IGalaxy           mGalaxy;
  private final IAI_Faction       mGameMaster = new AI_GameMasterFaction();
  private final List<IJG_Faction> mOrigFactions = new ArrayList<>(8);
  private final List<IJG_Faction> mFactions = new ArrayList<>(8);
  private final List<IJG_Player>  mPlayers = new ArrayList<>(8);

  private final List<String>      mMessages = new ArrayList<>(8);

  private       IJG_GameInfo mGameInfo;
  private       String    mGameType = "";
  private       long      mTurnNumber;
  private       long      mTurnIntervalSecs;
  private       long      mTurnHistory;
  private       boolean   mRunWhenAllOrdersAreIn;
  private       double    mTimeProgressionDays;
  private       String    mNextRun;
  private       boolean   mRealtime;
  private       long      mUTCGameTime = 0L;

  private JG_Game( String pName, IGalaxy pGalaxy ) {
    super(pName,pName);
    mGalaxy = pGalaxy;
    return;
  }

  @Override
  public IJG_GameInfo getGameInfo() {
    return mGameInfo;
  }

  @Override
  public String gameType() {
    return mGameType;
  }

  @Override
  public void setGameType(String pType) {
    mGameType = pType;
    return;
  }

  @Override
  public boolean runWhenAllOrdersAreIn() {
    return mRunWhenAllOrdersAreIn;
  }

  @Override
  public void setRunWhenAllOrdersAreIn(boolean pRunWhenAllOrdersAreIn) {
    mRunWhenAllOrdersAreIn = pRunWhenAllOrdersAreIn;
    return;
  }

  @Override
  public void setTurnHistory(long pTurnHistory) {
    mTurnHistory = pTurnHistory;
    return;
  }

  @Override
  public long turnHistory() {
    return mTurnHistory;
  }

  @Override
  public void setGameInfo(IJG_GameInfo pGameInfo) {
    mGameInfo = pGameInfo;
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
  public long turnIntervalSecs() {
    return mTurnIntervalSecs;
  }

  @Override
  public void setTurnIntervalSecs(long pIntervalSecs) {
    mTurnIntervalSecs = pIntervalSecs;
    return;
  }

  @Override
  public double timeProgressionDays() {
    return mTimeProgressionDays;
  }

  @Override
  public void setTimeProgressionDays(double pDays) {
    mTimeProgressionDays = pDays;
    return;
  }

  @Override
  public long UTCTime() {
    return mUTCGameTime;
  }

  @Override
  public void setUTCTime(long pTime) {
    mUTCGameTime = pTime;
    return;
  }

  @Override
  public void setRealTime(boolean pRealtime) {
    mRealtime = pRealtime;
    return;
  }

  @Override
  public boolean isRealTime() {
    return mRealtime;
  }

  @Override
  public String nextRun() {
    return mNextRun;
  }

  @Override
  public void setNextRun(String pNextRun) {
    mNextRun = pNextRun;
    return;
  }

  @Override
  public void removeTurnNumber(File pPath,long pNumber) {
    if (pNumber>0) {
      File f = new File(pPath, "game_" + pNumber + ".xml");
      f.delete();
      for (var faction : factions()) {
        faction.removeTurnNumber(new File(pPath, "factions"), pNumber);
      }
      for (var player : players()) {
        player.removeTurnNumber(new File(pPath, "players"), pNumber);
      }
    }
    return;
  }

  @Override
  public void addFaction(IJG_Faction pOrigFaction, IJG_Faction pFaction) {
    mOrigFactions.add(pOrigFaction);
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
  public IJG_Player getPlayerByName(String pName) {
    return mPlayers.stream().filter( p -> p.name().equals(pName)).findFirst().orElse(null);
  }

  @Override
  public IJG_Player getPlayerByID(String pID) {
    return mPlayers.stream().filter( p -> p.id().equals(pID)).findFirst().orElse(null);
  }

  @Override
  public IJG_Player getPlayerByUsername(String pUsername) {
    return mPlayers.stream().filter( p -> Objects.equals(p.getUsername(),pUsername)).findFirst().orElse(null);
  }

  @Override
  public List<String> messagesMutable() {
    return mMessages;
  }

  @Override
  public List<IJG_Faction> factions() {
    return mFactions;
  }

  @Override
  public IJG_Faction getFactionById(String pId) {
    return mFactions.stream().filter( p -> p.id().equals(pId)).findFirst().orElse(null);
  }

  @Override
  public IGalaxy galaxy() {
    return mGalaxy;
  }


  @Override
  public void timeProgression(IJG_Game pGame, Duration pTimeStep) {

    setUTCTime(pGame.UTCTime() + pTimeStep.toMillis());

    mGalaxy.timeProgression(pGame, pTimeStep);

    aiPhase();

    cleanUp();

    designPhase();
    joinPhase();
    // Planetary production orders are assigned. Note that production occurs later in the turn.
    producePhase();

    // Messages are sent.
    messagesPhase();
    // Alliances and war are declared.
    declarePhase();
    // Groups with weapons attack enemy ships, causing combat. This can happen if a player declares war on the current turn. It can also happen if a player built a ship with weapons at a planet with enemy ships in orbit at the end of the previous turn.
    fightPhase();

    // Groups with weapons bomb enemy planets. This can happen if a player declares war on the current turn.
    // Groups load or unload cargo.
    unloadPhase();
    loadPhase();

    // Groups are upgraded.
    upgradePhase();
    // Groups and fleets sent to planets enter hyperspace.

    // Routes are assigned to planets. Cargo ships are assigned cargos and destinations, load cargo (if necessary) and enter hyperspace.
    routesPhase();

    // Groups and fleets with intercept orders are assigned destinations and enter hyperspace.
    interceptPhase();
    // Groups and fleets move through hyperspace, possibly arriving at planets.
    movePhase();

    // Groups with weapons attack enemy ships, causing combat.
    fightPhase();

    // Groups with weapons bomb enemy planets.
    bombPhase();

    // Planets produce materials or capital, conduct research, or build ships.
    // Population growth occurs.
    planetProducePhase(pTimeStep);

    // All ships unload cargo if the autounload option is turned on.
    unloadPhase();

    // For players with the autounload option turned off, ships that are at route destinations unload cargo.
    // Identical groups are merged.
    combinePhase();

    // Groups are renumbered if the sortgroups option is turned on.
    // Races, planets, ships and fleets are renamed.
    renamePhase();

    roundUp();

    reconPhase(false);

    mTurnNumber++;

    return;
  }

  @Override
  public void aiPhase() {
    mGameMaster.createOrders(mGameInfo,this,null,null);
    mGameMaster.sendOrders();
    int ix=0;
    for( IJG_Faction faction : factions() ) {
      if (faction.getAI()!=null) {
        faction.getAI().createOrders(mGameInfo, this, mOrigFactions.get(ix),faction);
        if (!isRealTime()) {
          faction.getAI().sendOrders();
        }
      }

      ix++;
    }
    return;
  }

  /**
   * cleanUp
   */
  private void cleanUp() {
    for( IJG_Faction faction : factions() ) {
      for( IJG_Group group : new ArrayList<>(faction.groups().getGroups()) ) {
        if (group.getNumberOf()==0) {
          faction.groups().removeGroup(group);
        } else {
          group.shotsMutable().clear();
        }
      }
      faction.getMessagesMutable().clear();
      faction.getIncomingMutable().clear();
      faction.getBombingsMutable().clear();
      faction.getOtherFactionsMutable().clear();
    }
    return;
  }

  public void roundUp() {
    for( IJG_Faction faction : factions() ) {
      for (IJG_Planet planet : mGalaxy.map().planets().planets()) {
        IJG_Planet findplanet = faction.planets().findPlanetById(planet.id());
        if (findplanet==null) {
          faction.planets().addPlanet(planet);
        } else {
          findplanet.copyFrom(planet);
        }
      }

//      faction.planets().clear();
//      for (IJG_Planet planet : mGalaxy.map().planets().planets()) {
//        faction.planets().addPlanet(planet);
//      }
    }
    factions().stream().forEach( f -> f.doOrders(EPhase.ROUNDUP));
    return;
  }

  /**
   * reconPhase
   */
  @Override
  public void reconPhase( boolean pInit ) {
    // **** Planets visibility
    for( IJG_Faction faction : factions() ) {
      if (pInit || !isRealTime()) {
        faction.planets().replaceByCopyOf();
      }
      for( IJG_Planet planet : faction.planets().planetsNotOwnedBy(faction)) {
        IJG_Planet realplanet = mGalaxy.map().planets().findPlanetById(planet.id());
        // **** Own ships above?
//        if (faction.groups().groupsByPosition(realplanet.position()).isEmpty()) {
        if (!faction.groups().isGroupAtPosition(realplanet.position())) {
          if (realplanet.faction() == null) {
            planet.setPlanetToVisibility(FLOS_Visibility.VIS_MINIMUM, realplanet);
          } else {
            // **** Has a owner
            planet.setPlanetToVisibility(FLOS_Visibility.VIS_ID, realplanet);
          }
        } else {
          // **** Own ships above
          planet.setPlanetToVisibility(FLOS_Visibility.VIS_FULL, realplanet);
        }
      }
    }

    // **** Gather all recon positions
    for( IJG_Faction faction : factions() ) {
      Map<String,IJG_Faction> visOtherFactions = new HashMap<>(256);
      visOtherFactions.putAll(faction.getOtherFactionsMutable().stream().collect(Collectors.toMap(IEntity::id, f -> f)));
      Map<String, IJG_Position> reconPositions = new HashMap<>(64);
      for( var planet : faction.planets().planetsOwnedBy(faction)) {
        reconPositions.put(planet.position().toString(), planet.position());
      }
      for( var group : faction.groups().getGroups()) {
        reconPositions.put(group.position().toString(), group.position());
      }
      for( IJG_Faction otherfaction : factions() ) {
        if (otherfaction!=faction) {
          IJG_Faction visOtherFaction = visOtherFactions.computeIfAbsent(otherfaction.id(), k -> {
            IJG_Faction f = JG_Faction.of(this, otherfaction.id(),otherfaction.name());
            faction.addOtherFaction(f);
            return f;
          });
//          IJG_Faction visOtherFaction = JG_Faction.of(this, otherfaction.id(),otherfaction.name());
//          faction.getOtherFactionsMutable().add(visOtherFaction);
          // **** Check for visible fleets/groups
          for( IJG_Group group : otherfaction.groups().getGroups()) {
            if (reconPositions.containsKey(group.position().toString())) {
              visOtherFaction.groups().addGroup(group);
              visOtherFaction.addUnitDesign(otherfaction.getUnitDesignById(group.unitDesign()));
            }
          }
        }
      }
    }


//    // **** Other factions
//    for( IJG_Faction faction : factions() ) {
//      faction.getOtherFactionsMutable().clear();
//      for (IJG_Faction otherfaction : factions()) {
//        if (otherfaction!=faction) {
//          IJG_Faction visOtherFaction = JG_Faction.of(this, otherfaction.id(),otherfaction.name());
//          faction.getOtherFactionsMutable().add(visOtherFaction);
//          // **** Check for visible fleets/groups
//          for( IJG_Group group : otherfaction.groups().getGroups()) {
//            // **** Orbit above planet?
//            for( var planet : faction.planets().planets()) {
//              // **** Own planet
//              if (Objects.equals(planet.faction(),faction.id())) {
//                if (planet.position().equals(group.position())) {
//                  visOtherFaction.groups().addGroup(group);
//                  visOtherFaction.addUnitDesign(otherfaction.getUnitDesignById(group.unitDesign()));
//                }
//              } else {
//                // **** Or an own fleet is in orbit
//                for( var g : faction.groups().groupsByPosition(planet.position()).getGroups()) {
//                  if (g.position().equals(group.position())) {
//                    visOtherFaction.groups().addGroup(group);
//                    visOtherFaction.addUnitDesign(otherfaction.getUnitDesignById(group.unitDesign()));
//                  }
//                }
////                if (!faction.groups().groupsByPosition(planet.position()).isEmpty()) {
////                  visOtherFaction.groups().addGroup(group);
////                }
//              }
//            }
//          }
//        }
//      }
//    }

    return;
  }

  private void renamePhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.RENAME));
    return;
  }

  private void combinePhase() {
    for( IJG_Faction faction : mFactions ) {
      if (faction.groups().combineGroups()) {
        faction.newChange();
      }
    }
//    mFactions.stream().forEach( f -> f.groups().combineGroups() );
    return;
  }

  private void messagesPhase() {
    return;
  }

  private void planetProducePhase( Duration pTimeStep ) {
    for( IJG_Planet planet : mGalaxy.map().planets().planets() ) {
      planet.timeProgression(this, pTimeStep);
    }
    return;
  }

  private void routesPhase() {
    return;
  }

  private void declarePhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.DECLAREWAR));
    factions().stream().forEach( f -> f.doOrders(EPhase.DECLAREALLIANCE));
    return;
  }

  private void designPhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.DESIGN));
    return;
  }

  private void producePhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.PLANET_PRODUCTION));
    return;
  }

  private void joinPhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.JOIN));
    return;
  }

  private void fightPhase() {
    var planets = mGalaxy.map().planets().planets();
    Collections.shuffle(planets);
    SB_Battle.performPlanetGroupBattles(this,planets);
    return;
  }

  private void upgradePhase() {
    return;
  }

  private void fleetPhase() {
  }

  private void bombPhase() {
    SB_Battle.bombPlanets(this,mGalaxy.map().planets().planets());
    return;
  }

  private void unloadPhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.UNLOAD));
    return;
  }

  private void loadPhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.LOAD));
    return;
  }

  private void interceptPhase() {
  }

  private void movePhase() {
    factions().stream().forEach( f -> f.doOrders(EPhase.SEND));
    factions().stream().forEach( f -> f.groups().moveGroups(this,f));
    return;
  }

  /**
   * storeObject
   * @param pPath
   * @param pParent
   * @param pName
   */
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
    Element gamenode = doc.createElement( "game" );
    gamenode.setAttribute("name", name() );
    gamenode.setAttribute("gameType", gameType() );
    gamenode.setAttribute("turnNumber", ""+turnNumber() );
    gamenode.setAttribute("turnHistory", ""+turnHistory() );
    gamenode.setAttribute("turnIntervalSecs", ""+turnIntervalSecs() );
    gamenode.setAttribute("nextRun", nextRun() );
    gamenode.setAttribute("realtime", ""+isRealTime() );
    gamenode.setAttribute("timeProgressionDays", ""+timeProgressionDays() );
    gamenode.setAttribute("runWhenAllOrdersAreIn", ""+runWhenAllOrdersAreIn() );
    gamenode.setAttribute("gameTime", ""+UTCTime() );


    for( String msg : mMessages ) {
      Node n = doc.createElement("message");
      n.setTextContent(msg);
      gamenode.appendChild(n);
    }

    try {
      mGalaxy.storeObject( null, gamenode, "", "" );
      if (pPath!=null) {
        for (var faction : factions()) {
          faction.storeObject(new File(pPath, "factions"), gamenode, "", "");
        }
      }
      if (pPath==null) {
        Node playersNode = doc.createElement("players");
        for (var player : players()) {
          player.storeObject(pPath, playersNode, "", "");
        }
        gamenode.appendChild(playersNode);
      } else {
        for (var player : players()) {
          player.storeObject(new File(pPath, "players"), gamenode, "", "");
        }
      }
      root.appendChild(gamenode);
      if (pPath!=null) {
        String gamexml = XML_Utils.documentToString(doc);
        GEN_Streams.writeStringToFile(gamexml, new File(pPath, "game_" + mTurnNumber + ".xml"));
      }
//      if (pParent!=null) {
//        pParent.appendChild(root);
//      }
    } catch (IOException | TransformerException e ) {
      e.printStackTrace();
    }
    return;
  }

  @Override
  public IJG_Game copyOf() {
    JG_Game game = new JG_Game(name(),mGalaxy.copyOf());

    game.mOrigFactions.addAll( mOrigFactions.stream().map(f -> f.copyOf(game)).toList() );
    game.mFactions.addAll( mFactions.stream().map(f -> f.copyOf(game)).toList() );
    game.mPlayers.addAll( mPlayers.stream().map( IJG_Player::copyOf).toList() );
    game.mMessages.addAll( mMessages );
//
//    private       IJG_GameInfo mGameInfo;
//    private       long      mTurnNumber;
//    private       long      mTurnIntervalSecs;
//    private       long      mTurnHistory;
//    private       boolean   mRunWhenAllOrdersAreIn;
//    private       double    mTimeProgressionDays;
//    private       String    mNextRun;
//    private       boolean   mRealtime;


    return game;
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

  @Override
  public IJG_Groups groupsByPosition(IJG_Position pPosition) {
    IJG_Groups groups = JG_Groups.of();
    for( IJG_Faction faction : factions() ) {
      groups.addGroups(faction.groups().groupsByPosition(pPosition));
    }
    return groups;
  }

  @Override
  public void prepareGameAsUser(String pUserName) {
    var player = getPlayerByUsername(pUserName);
    if (player==null) {
      mGalaxy.map().planets().clear();
    } else {
      for( IJG_Planet planet : mGalaxy.map().planets().planets()) {
        planet.anonymize();
      }
    }
    return;
  }

  @Override
  public List<IJG_Faction> topFactions() {
    List<IJG_Faction> topFactions = new ArrayList<>(16);
    // **** TopFactions
    for( IJG_Player player : players()) {
      for( IJG_Faction faction : player.factions() ) {
        topFactions.add(faction);
      }
    }
    Collections.sort(topFactions, Comparator.comparingDouble( IJG_Faction::getReconTotalPop));
    topFactions = topFactions.reversed();
    return topFactions;
  }

  private String reportPlain(IJG_Player pPlayer ) {
    String report = "";

    // ****
    report += "\t\t\tStatus of Players\n";
    report += "\n";
    report += "N\t\t\tD\tW\tS\tC\tP\tI\t#\tR\n";
    for( IJG_Faction faction : factions() ) {
      report += faction.name() + "\n";
    }
    report += "\n";

    // **** Mistakes
    report += "\tMISTAKES\n";
    for( IJG_Faction faction : factions() ) {
      for( OrderException orderException : faction.orderExceptions()) {
        report += orderException.getOrder().toString() + "\n";
        report += orderException.getMessage() + "\n";
      }
    }
    report += "\n";


    // ****
    report += "\t\t\tYour Ship Types\n";
    report += "\n";
    report += "N\t\t\tD\tA\tW\tS\tC\tMass\tSpeed\tDef\n";
    report += "\n";

    List<IJG_Planet> planets = galaxy().map().planets().planets();

    // ****
    report += "\t\t\tYour Planets\n";
    report += "\n";
    report += "N                     X      Y      S       P       I      R         P          $     M      C      L\n";
    for( IJG_Planet planet : new ArrayList<>(planets) ) {
      if (planet.visibilityFor(this,pPlayer)>= FLOS_Visibility.VIS_FULL) {
        report += planet.formatString() + "\n";
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
