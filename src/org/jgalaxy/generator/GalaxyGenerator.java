package org.jgalaxy.generator;

import org.jgalaxy.Galaxy;
import org.jgalaxy.IGalaxy;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.ai.AI_Faction;
import org.jgalaxy.engine.*;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.map.MAP_Map;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.planets.JG_Planet;
import org.jgalaxy.utils.GEN_Math;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class GalaxyGenerator {

  static private boolean isValidPlanetPosition(IJG_Position newPos, List<IJG_Planet> existingPlanets, IGalaxyTemplate pTemplate ) {
    if (existingPlanets.isEmpty()) {
      return true;
    }
    for (IJG_Planet existing : existingPlanets) {
      double dist = GEN_Math.distance(existing, newPos);
      if (dist<pTemplate.minDistBetweenPlanets()) {
        return false;
      }
    }
    return true;
  }

  static private boolean isValidHomePlanetPosition(IJG_Position newPos, List<IJG_Planet> existingPlanets, IGalaxyTemplate pTemplate ) {
    if (existingPlanets.isEmpty()) {
      return true;
    }
    for (IJG_Planet existing : existingPlanets) {
      double dist = GEN_Math.distance(existing, newPos);
      if (dist<pTemplate.minDistBetweenHomePlanets()) {
        return false;
      }
    }
    return true;
  }

  static public IJG_Game generate( IGalaxyTemplate pGalaxyTemplate ) {

    GEN_Math math = new GEN_Math(pGalaxyTemplate.seed());

    IMAP_Map map = MAP_Map.of(pGalaxyTemplate.xStart(), pGalaxyTemplate.yStart(), pGalaxyTemplate.xEnd(), pGalaxyTemplate.yEnd());
    IGalaxy galaxy = Galaxy.of(map);

    IJG_Game game = JG_Game.of( pGalaxyTemplate.name(), galaxy );
    Node gameNode = pGalaxyTemplate.gameNode();
    game.setGameType(XML_Utils.attr(gameNode, "gameType",""));
    game.setTimeProgressionDays( Double.parseDouble(XML_Utils.attr(gameNode, "timeProgressionDays", "365" )));
    game.setTurnIntervalSecs( Integer.parseInt(XML_Utils.attr(gameNode, "turnIntervalSecs", "-1" )));
    game.setTurnHistory( Integer.parseInt(XML_Utils.attr(gameNode, "turnHistory", "-1" )));
    game.setRunWhenAllOrdersAreIn(Boolean.valueOf(XML_Utils.attr(gameNode, "runWhenAllOrdersAreIn", "false" )));
    game.setRealTime(Boolean.valueOf(XML_Utils.attr(gameNode, "realtime", "false" )));


    // **** playerGenerations
    List<IJG_Planet> homeplanets = new ArrayList<>(8);
    for( Node pgen : pGalaxyTemplate.playerGenerations()) {
      for (int ix = 0; ix < Integer.parseInt(XML_Utils.attr(pgen, "generate")); ix++) {
        IJG_Player player = JG_Player.of( game, "player" + ix,null,"player" + ix, "player" + ix );
        IJG_Faction faction = JG_Faction.of( game, "faction" + ix, "faction" + ix );
        faction.setAI(new AI_Faction());
        player.addFaction( faction );
        game.addFaction( faction,faction );
        game.addPlayer(player);
        IJG_Planet homeplanet = null;
        int i = 0;
        for( ; i<800000; i++) {
          IJG_Position pos = JG_Position.of(math.nextRandom() * (map.xEnd() - map.xStart()), math.nextRandom() * (map.yEnd() - map.yStart()));
          if (isValidHomePlanetPosition(pos,homeplanets,pGalaxyTemplate)) {
            homeplanet = JG_Planet.of("home" + ix, "home" + ix, pos);
            homeplanet.setSize(pGalaxyTemplate.homePlanetMin() + Math.random() * (pGalaxyTemplate.homePlanetMax() - pGalaxyTemplate.homePlanetMin()));
            homeplanet.setResources(10);
            homeplanet.setPopulation(homeplanet.size());
            homeplanet.setIndustry(homeplanet.size());
            homeplanet.setFaction(faction.id());
            homeplanets.add(homeplanet);
            break;
          }
        }
        if (i==800000) {
          System.out.printf("Cannot generate Home planets (" + ix + ")");
          return null;
        }
        faction.planets().addPlanet( homeplanet );
        galaxy.map().planets().addPlanet( homeplanet );
      }
    }

    IJG_Planets planets = galaxy.map().planets();
    int np = 0;
    for( Node pgen : pGalaxyTemplate.planetGenerations()) {
      for(int ix = 0; ix<Integer.parseInt(XML_Utils.attr(pgen, "generate")); ix++) {
        for( int i=0; i<80000; i++) {
          IJG_Position pos = JG_Position.of(math.nextRandom() * (map.xEnd() - map.xStart()), math.nextRandom() * (map.yEnd() - map.yStart()));
          if (isValidPlanetPosition(pos,planets.planets(),pGalaxyTemplate)) {
            IJG_Planet planet = JG_Planet.of("" + ix, "" + ix, pos );
            planet.setSize( pGalaxyTemplate.planetMinSize() + math.nextRandom()*(pGalaxyTemplate.planetMaxSize()-pGalaxyTemplate.planetMinSize()) );
            planet.setResources(math.nextRandom()*10);
            planets.addPlanet( planet );
            np++;
            break;
          }
        }
      }
    }
    System.out.printf("Planet generated = " + np);

    return game;
  }

  private GalaxyGenerator() {
    return;
  }

}
