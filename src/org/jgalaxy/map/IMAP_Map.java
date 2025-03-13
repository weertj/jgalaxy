package org.jgalaxy.map;

import org.jgalaxy.IStorage;
import org.jgalaxy.ITimeProgression;
import org.jgalaxy.planets.IJG_Planet;

import java.util.List;

public interface IMAP_Map extends ITimeProgression, IStorage {

  void addPlanet( IJG_Planet pPlanet );

  IJG_Planet planetById( String pID );
  IJG_Planet planetByName( String pName );
  List<IJG_Planet> planets();

}
