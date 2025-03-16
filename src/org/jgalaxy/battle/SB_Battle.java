package org.jgalaxy.battle;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.planets.IJG_Planet;

import java.util.List;

public class SB_Battle {


  /****f* Battle/performPlanetGroupBattles
   * NAME
   *   fightphase
   * SYNOPSIS
   *   void fightphase(game *aGame, int phase)
   * FUNCTION
   *   Itterates over all planets and checks where any battles
   *   need to take place. Allocates and initializes the necessary
   *   battle structures and calls the function to carry out the battle.
   *
   *   The code works on three basic structures:
   *     battle       keeps track of each battle.
   *     participant  keeps track of the participants in each battle, contains
   *                  the groups that take part in the battle.
   *     batstat      keeps track of the battle statistics during a battle.
   *     group        keeps track of which ship in the group fired, which
   *                  have been shot, and how many there are left that can fire.
   * INPUTS
   *   aGame --
   *   phase -- there are two fight phases, this tells which phase we are
   *            in. Is used for the battle protocol option.
   * SOURCE
   */
  static public void performPlanetGroupBattles(IJG_Game pGame, List<IJG_Planet> pPlanets) {
    for( IJG_Planet planet : pPlanets ) {
      ISB_BattleField field = createBattleField(pGame,planet);
      if (field.isBattle()) {
        boolean rerun = true;
        while( rerun ) {
          rerun = field.battleRound();
        }
      }
    }
    return;
  }

  static public ISB_BattleField createBattleField(IJG_Game pGame,IJG_Planet pPlanet) {
    ISB_BattleField field = SB_BattleField.of(pGame);
    var groups = pGame.groupsByPosition(pPlanet.position());
    if (!groups.isEmpty()) {
      for (var faction : pGame.factions()) {
        var factionGroups = groups.groupsByFaction(faction);
        if (!factionGroups.isEmpty()) {
          field.addEntry(faction, factionGroups );
        }
      }
    }
    return field;
  }


  private SB_Battle() {
    return;
  }

}
