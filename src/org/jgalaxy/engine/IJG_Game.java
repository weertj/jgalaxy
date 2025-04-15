package org.jgalaxy.engine;

import org.jgalaxy.*;
import org.jgalaxy.units.IJG_Groups;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public interface IJG_Game extends IEntity,ITimeProgression, IStorage {

  void roundUp();
  void reconPhase();
  void aiPhase();

  void setGameInfo( IJG_GameInfo pGameInfo);
  IJG_GameInfo getGameInfo();

  long turnNumber();
  void setTurnNumber(long pNumber);

  long turnIntervalSecs();
  void setTurnIntervalSecs(long pIntervalSecs);
  boolean runWhenAllOrdersAreIn();
  void setRunWhenAllOrdersAreIn(boolean pRunWhenAllOrdersAreIn);


  double timeProgressionDays();
  void setTimeProgressionDays(double pDays);

  String nextRun();
  void setNextRun(String pNextRun);
  default void calcNextRun() {
    ZonedDateTime now = ZonedDateTime.now(ZoneId.of( "UTC" ));
    if (turnIntervalSecs()>0) {
      now.plusSeconds(turnIntervalSecs());
      setNextRun(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(now));
    } else {
      setNextRun(null);
    }
    return;
  }

  void addFaction(IJG_Faction pOrigFaction,IJG_Faction pFaction);
  void addPlayer(IJG_Player pPlayer);

  List<IJG_Faction> factions();
  IJG_Faction getFactionById(String pId );

  List<IJG_Player> players();
  IJG_Player getPlayerByName( String pName );
  IJG_Player getPlayerByID( String pID );
  IJG_Player getPlayerByUsername( String pUsername );

  IGalaxy galaxy();

  String reportForPlayerAs( IJG_Player pPlayer, String pFormat );

  IJG_Groups groupsByPosition(IJG_Position pPosition);

  void prepareGameAsUser( String pUserName );

  List<String> messagesMutable();


  List<IJG_Faction> topFactions();


}
