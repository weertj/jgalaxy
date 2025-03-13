package org.jgalaxy.planets;

import java.util.ArrayList;
import java.util.List;

public class JG_Planets implements IJG_Planets {

  private final List<IJG_Planet> mPlanets = new ArrayList<>(8);

  public JG_Planets(List<IJG_Planet> pPlanets) {
    mPlanets.addAll(pPlanets);
    return;
  }

  @Override
  public void addPlanet(IJG_Planet pPlanet) {
    mPlanets.add(pPlanet);
    return;
  }

  @Override
  public IJG_Planet findPlanetById(String pId) {
    return mPlanets.stream().filter(p -> p.id().equals(pId)).findFirst().orElse(null);
  }

  @Override
  public List<IJG_Planet> planets() {
    return mPlanets;
  }


}
