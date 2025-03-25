package org.jgalaxy.generator;

import org.jgalaxy.Galaxy;
import org.jgalaxy.IGalaxy;
import org.jgalaxy.JG_Position;
import org.jgalaxy.engine.*;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.map.MAP_Map;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.JG_Planet;

public class GalaxyGenerator {

  static public IJG_Game generate( IGalaxyTemplate pGalaxyTemplate ) {

    IMAP_Map map = MAP_Map.of(pGalaxyTemplate.xStart(), pGalaxyTemplate.yStart(), pGalaxyTemplate.xEnd(), pGalaxyTemplate.yEnd());
    IGalaxy galaxy = Galaxy.of(map);

    for( int ix=0; ix<20; ix++ ) {
      IJG_Planet planet = JG_Planet.of("planet" + ix, "planet" + ix,
        JG_Position.of( Math.random()*(map.xEnd()-map.xStart()), Math.random()*(map.yEnd()-map.yStart()) ));
      planet.setSize( Math.random()*1000 );
      galaxy.map().planets().addPlanet( planet );
    }

    IJG_Game game = JG_Game.of( pGalaxyTemplate.name(), galaxy );

    for( int ix=0; ix<4; ix++ ) {
      IJG_Player player = JG_Player.of( game, "player" + ix, "player" + ix );
      IJG_Faction faction = JG_Faction.of( game, "faction" + ix, "faction" + ix );
      player.addFaction( faction );
      game.addFaction( faction );
      game.addPlayer(player);

      IJG_Planet planet = JG_Planet.of("home" + ix, "home" + ix,
        JG_Position.of( Math.random()*(map.xEnd()-map.xStart()), Math.random()*(map.yEnd()-map.yStart()) ));
      planet.setSize( 1000 );
      planet.setPopulation(1000);
      planet.setOwner(faction.id());
      faction.planets().addPlanet( planet );
      galaxy.map().planets().addPlanet( planet );
    }

    return game;
  }

  private GalaxyGenerator() {
    return;
  }

}
