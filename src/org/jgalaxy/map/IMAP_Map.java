package org.jgalaxy.map;

import org.jgalaxy.ITimeProgression;
import org.jgalaxy.planets.IJG_Planet;

import java.util.List;

public interface IMAP_Map extends ITimeProgression {

  void addPlanet( IJG_Planet pPlanet );

  List<IJG_Planet> planets();

}
