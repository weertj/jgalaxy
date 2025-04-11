package org.jgalaxy.generator;

import org.jgalaxy.Galaxy;
import org.jgalaxy.IGalaxy;
import org.jgalaxy.JG_Position;
import org.jgalaxy.ai.AI_Faction;
import org.jgalaxy.engine.*;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.map.MAP_Map;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.JG_Planet;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Node;

public class GalaxyGenerator {

  static public IJG_Game generate( IGalaxyTemplate pGalaxyTemplate ) {

    IMAP_Map map = MAP_Map.of(pGalaxyTemplate.xStart(), pGalaxyTemplate.yStart(), pGalaxyTemplate.xEnd(), pGalaxyTemplate.yEnd());
    IGalaxy galaxy = Galaxy.of(map);

    for( Node pgen : pGalaxyTemplate.planetGenerations()) {
      for(int ix = 0; ix<Integer.parseInt(XML_Utils.attr(pgen, "generate")); ix++) {
        IJG_Planet planet = JG_Planet.of("" + ix, "" + ix,
          JG_Position.of( Math.random()*(map.xEnd()-map.xStart()), Math.random()*(map.yEnd()-map.yStart()) ));
        planet.setSize( Math.random()*1000 );
        planet.setResources(Math.random()*10);
        galaxy.map().planets().addPlanet( planet );
      }
    }
//    for( int ix=0; ix<120; ix++ ) {
//      IJG_Planet planet = JG_Planet.of("planet" + ix, "planet" + ix,
//        JG_Position.of( Math.random()*(map.xEnd()-map.xStart()), Math.random()*(map.yEnd()-map.yStart()) ));
//      planet.setSize( Math.random()*1000 );
//      galaxy.map().planets().addPlanet( planet );
//    }

    IJG_Game game = JG_Game.of( pGalaxyTemplate.name(), galaxy );
    Node gameNode = pGalaxyTemplate.gameNode();
    game.setTimeProgressionDays( Double.parseDouble(XML_Utils.attr(gameNode, "timeProgressionDays", "365" )));
    game.setTurnIntervalSecs( Integer.parseInt(XML_Utils.attr(gameNode, "turnIntervalSecs", "-1" )));
    game.setRunWhenAllOrdersAreIn(Boolean.valueOf(XML_Utils.attr(gameNode, "runWhenAllOrdersAreIn", "false" )));

    for( Node pgen : pGalaxyTemplate.playerGenerations()) {
      for (int ix = 0; ix < Integer.parseInt(XML_Utils.attr(pgen, "generate")); ix++) {
        IJG_Player player = JG_Player.of( game, null,null,"player" + ix, "player" + ix );
        IJG_Faction faction = JG_Faction.of( game, "faction" + ix, "faction" + ix );
        faction.setAI(new AI_Faction());
        player.addFaction( faction );
        game.addFaction( faction,faction );
        game.addPlayer(player);
        IJG_Planet planet = JG_Planet.of("home" + ix, "home" + ix,
          JG_Position.of( Math.random()*(map.xEnd()-map.xStart()), Math.random()*(map.yEnd()-map.yStart()) ));
        planet.setSize( 1000 );
        planet.setResources(10);
        planet.setPopulation(1000);
        planet.setIndustry(1000);
        planet.setFaction(faction.id());
        faction.planets().addPlanet( planet );
        galaxy.map().planets().addPlanet( planet );
      }
    }

//    for( int ix=0; ix<20; ix++ ) {
//      IJG_Player player = JG_Player.of( game, null,null,"player" + ix, "player" + ix );
//      IJG_Faction faction = JG_Faction.of( game, "faction" + ix, "faction" + ix );
//      player.addFaction( faction );
//      game.addFaction( faction );
//      game.addPlayer(player);
//
//      IJG_Planet planet = JG_Planet.of("home" + ix, "home" + ix,
//        JG_Position.of( Math.random()*(map.xEnd()-map.xStart()), Math.random()*(map.yEnd()-map.yStart()) ));
//      planet.setSize( 1000 );
//      planet.setPopulation(1000);
//      planet.setIndustry(1000);
//      planet.setFaction(faction.id());
//      faction.planets().addPlanet( planet );
//      galaxy.map().planets().addPlanet( planet );
//    }

    return game;
  }

  private GalaxyGenerator() {
    return;
  }

}
