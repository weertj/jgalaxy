package org.jgalaxy.engine;

import org.jgalaxy.*;
import org.jgalaxy.orders.IJG_Order;
import org.jgalaxy.units.IJG_Groups;

import java.io.File;
import java.util.List;


public interface IJG_Game extends ITimeProgression, IStorage {

  long turnNumber();
  void setTurnNumber(long pNumber);

  void addFaction(IJG_Faction pFaction);
  void addPlayer(IJG_Player pPlayer);

  List<IJG_Faction> factions();
  IJG_Faction getFactionById( String pId );

  List<IJG_Player> players();
  IJG_Player getPlayerByName( String pName );
  IJG_Player getPlayerByID( String pID );

  IGalaxy galaxy();

  String reportForPlayerAs( IJG_Player pPlayer, String pFormat );

  IJG_Groups groupsByPosition(IJG_Position pPosition);

}
