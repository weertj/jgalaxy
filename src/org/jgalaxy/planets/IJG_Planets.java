package org.jgalaxy.planets;

import java.util.List;

public interface IJG_Planets {

  void addPlanet(IJG_Planet pPlanet);
  IJG_Planet findPlanetById( String pId );
  IJG_Planet findPlanetByName( String pName );
  List<IJG_Planet> planets();

}
