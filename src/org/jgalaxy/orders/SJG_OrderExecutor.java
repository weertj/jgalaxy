package org.jgalaxy.orders;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.planets.EProduceType;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.units.IJG_Group;

public class SJG_OrderExecutor {

  static public void exec(IJG_Faction pFaction, IJG_Order pOrder, IJG_Game pGame ) {
    switch (pOrder.order()) {
      case RENAME -> {
      }

      // **** PRODUCE
      case PRODUCE -> orderPRODUCE(pGame,pFaction,pOrder);
      // **** SEND
      case SEND -> orderSEND(pGame,pFaction,pOrder);
      // **** SEND
      case WAR -> orderWAR(pGame,pFaction,pOrder);

    }
    return;
  }

  static private void orderPRODUCE(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) {
    String planetid = pOrder.param(0);
    String produce  = pOrder.param(1);
    IJG_Planet planet = pFaction.planets().findPlanetById(planetid);
    if (planet == null) {

    } else {
      if (produce.toUpperCase().equals("CAP")) {
        planet.setProduceType( EProduceType.PR_CAP, null );
      } else if (produce.toUpperCase().equals("MAT")) {
        planet.setProduceType( EProduceType.PR_MAT, null );
      } else if (produce.toUpperCase().equals("DRIVE")) {
        planet.setProduceType( EProduceType.PR_DRIVE, null );
      } else if (produce.toUpperCase().equals("WEAPONS")) {
        planet.setProduceType( EProduceType.PR_WEAPONS, null );
      } else if (produce.toUpperCase().equals("SHIELDS")) {
        planet.setProduceType( EProduceType.PR_SHIELDS, null );
      } else if (produce.toUpperCase().equals("CARGO")) {
        planet.setProduceType( EProduceType.PR_CARGO, null );
      } else {
        planet.setProduceType( EProduceType.PR_SHIP, produce );
      }
    }
    return;
  }

  static private void orderSEND(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) {
    String groupfleetid = pOrder.param(0 );
    IJG_Group group = pFaction.groups().getGroupById(groupfleetid);
    if (group == null) {

    } else {
      String planetid = pOrder.param(1);
      IJG_Planet planet = pGame.galaxy().map().planets().findPlanetById(planetid);
      if (planet == null) {
      } else {
        group.toPosition().copyOf(planet.position());
      }
    }
    return;
  }

  static private void orderWAR(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) {
    String factionid = pOrder.param(0 );
    pFaction.addWarWith(factionid);
    return;
  }


  /**
   * orderRENAME
   * @param pOrder
   * @param pGame
   */
  static private void orderRENAME( IJG_Order pOrder, IJG_Game pGame ) {
    IJG_Planet planet = pGame.galaxy().map().planets().findPlanetByName(pOrder.parameters().get(0) );
    if (planet!=null) {
      planet.rename( pOrder.parameters().get(1) );
    }
    return;
  }

  private SJG_OrderExecutor() {
    return;
  }

}
