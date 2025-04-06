package org.jgalaxy.ai;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.IJG_GameInfo;

public interface IAI_Faction {

  void createOrders(IJG_GameInfo pGameInfo,IJG_Game pGame, IJG_Faction pOrigFaction, IJG_Faction pFaction);
  void sendOrders();

}
