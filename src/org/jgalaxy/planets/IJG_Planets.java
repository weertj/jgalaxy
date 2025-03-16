package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;

import java.util.List;

public interface IJG_Planets {

  void clear();

  void addPlanet(IJG_Planet pPlanet);
  IJG_Planet findPlanetById( String pId );
  IJG_Planet findPlanetByName( String pName );
  IJG_Planet findPlanetByPosition(IJG_Position pPosition );
  List<IJG_Planet> planets();

}
