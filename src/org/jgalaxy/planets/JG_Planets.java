package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;

import java.util.ArrayList;
import java.util.List;

public class JG_Planets implements IJG_Planets {

  private final List<IJG_Planet> mPlanets = new ArrayList<>(8);

  public JG_Planets(List<IJG_Planet> pPlanets) {
    mPlanets.addAll(pPlanets);
    return;
  }

  @Override
  public void clear() {
    mPlanets.clear();
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
  public IJG_Planet findPlanetByName(String pName) {
    return mPlanets.stream().filter(p -> p.name().equals(pName)).findFirst().orElse(null);
  }

  @Override
  public IJG_Planet findPlanetByPosition(IJG_Position pPosition) {
    return mPlanets.stream().filter(p -> p.position().equals(pPosition)).findFirst().orElse(null);
  }

  @Override
  public List<IJG_Planet> planetsOwnedBy(IJG_Faction pFaction) {
    return mPlanets.stream().filter(p -> pFaction.id().equals(p.owner())).toList();
  }

  @Override
  public List<IJG_Planet> planets() {
    return new ArrayList<>(mPlanets);
  }


}
