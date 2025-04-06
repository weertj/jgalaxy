package org.jgalaxy.battle;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.JG_Bombing;

import java.util.List;

public class SB_Battle {


  /**
   * bombPlanets
   * @param pGame
   * @param pPlanets
   */
  static public void bombPlanets( IJG_Game pGame, List<IJG_Planet> pPlanets ) {
    for(IJG_Faction faction : pGame.factions() ) {
      for(IJG_Group group : faction.groups().getGroups() ) {
        if (group.getNumberOf()>0 && faction.getUnitDesignById(group.unitDesign()).weapons()>0) {
          for (IJG_Planet planet : pPlanets) {
            if (planet.position().equals(group.position())) {
              if (planet.faction() != null && !group.faction().equals(planet.faction())) {
                IJG_Faction planetfaction = pGame.getFactionById(planet.faction());
                if (pGame.getFactionById(group.faction()).atWarWith().contains(planet.faction())) {
                  group.shotsMutable().add( new B_Shot(
                    IB_Shot.TYPE.SHIP_PLANET,
                    -1,planet.id(),planet.faction(),1));
                  planet.setFaction(null);
                  planet.setPopulation(0);
                  planet.setIndustry(0);
                  planet.setCapitals(0);
                  planet.setMaterials(0);
                  planet.setSpent(0);
                  planet.setProduceType(null,null);
                  faction.addBombing( new JG_Bombing(group.faction(), group.id(), group.position() ));
                  planetfaction.addBombing( new JG_Bombing(group.faction(), group.id(), group.position() ));
                }
              }
            }
          }
        }
      }
    }
    return;
  }

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
