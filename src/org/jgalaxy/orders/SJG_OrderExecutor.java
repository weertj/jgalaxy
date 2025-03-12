package org.jgalaxy.orders;

import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.planets.IJG_Planet;

public class SJG_OrderExecutor {

  static public void exec(IJG_Order pOrder, IJG_Game pGame ) {
    switch (pOrder.order()) {
      case RENAME -> {
      }
    }
    return;
  }

  /**
   * orderRENAME
   * @param pOrder
   * @param pGame
   */
  static private void orderRENAME( IJG_Order pOrder, IJG_Game pGame ) {
    IJG_Planet planet = pGame.galaxy().map().planetByName( pOrder.parameters().get(0) );
    if (planet!=null) {
      planet.rename( pOrder.parameters().get(1) );
    }
    return;
  }

  private SJG_OrderExecutor() {
    return;
  }

}
