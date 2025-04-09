package org.jgalaxy.ai;

import org.jgalaxy.common.C_Message;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.IJG_GameInfo;

public class AI_GameMasterFaction extends AI_Faction {

  public AI_GameMasterFaction() {
    super();
  }

  @Override
  public void createOrders(IJG_GameInfo pGameInfo, IJG_Game pGame, IJG_Faction pOrigFaction, IJG_Faction pFaction) {
    if (pGame.turnNumber()==0) {
      // **** Start
      for( IJG_Faction faction : pGame.factions()) {
        faction.getMessagesMutable().add(new C_Message(true,"Game " + pGame.name() + " is starting." ));
      }

    }
    return;
  }


}
