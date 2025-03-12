package org.jgalaxy.engine;

import org.jgalaxy.IGalaxy;
import org.jgalaxy.ITimeProgression;
import org.jgalaxy.orders.IJG_Order;

import java.util.List;


public interface IJG_Game extends ITimeProgression {

  void addFaction(IJG_Faction pFaction);
  void addPlayer(IJG_Player pPlayer);

  List<IJG_Faction> factions();
  List<IJG_Player> players();

  IGalaxy galaxy();

  void executeOrder(IJG_Order pOrder);


  String reportForPlayerAs( IJG_Player pPlayer, String pFormat );


}
