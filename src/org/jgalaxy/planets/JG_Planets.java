package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.utils.GEN_Math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JG_Planets implements IJG_Planets {

  private final List<IJG_Planet> mPlanets = new ArrayList<>(8);
  private final Map<String, IJG_Planet> mMapPlanets = new HashMap<>(256);

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
  public void replaceByCopyOf() {
    var planets = mPlanets.stream().map(IJG_Planet::copyOf).toList();
    mPlanets.clear();
    mPlanets.addAll(planets);
    return;
  }

  @Override
  public void addPlanet(IJG_Planet pPlanet) {
    mPlanets.add(pPlanet);
    return;
  }

  @Override
  public void removePlanet(IJG_Planet pPlanet) {
    mPlanets.remove(pPlanet);
    return;
  }

  @Override
  public IJG_Planet findPlanetById(String pId) {
//    if (mMapPlanets.isEmpty()) {
//      for( IJG_Planet planet : mPlanets ) {
//        mMapPlanets.put(planet.id(),planet);
//      }
//    }
//    return mMapPlanets.get(pId);
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
    return mPlanets.stream().filter(p -> pFaction.id().equals(p.faction())).toList();
  }

  @Override
  public List<IJG_Planet> planetsNotOwnedBy(IJG_Faction pFaction) {
    return mPlanets.stream().filter(p -> !pFaction.id().equals(p.faction())).toList();
  }

  @Override
  public IJG_Planet planetByIndex(int pIndex) {
    return mPlanets.get(pIndex);
  }

  @Override
  public List<IJG_Planet> planetsInTheRange(IJG_Position pPosition, double pRange) {
    return mPlanets.stream().filter( p ->GEN_Math.distance(pPosition,p)<=pRange ).toList();
  }

  @Override
  public List<IJG_Planet> planets() {
    return new ArrayList<>(mPlanets);
  }

  @Override
  public void sortByDistanceFrom(IJG_Position pFrom) {
    IJG_Position.sortByDistance(mPlanets, pFrom );
    return;
  }

  @Override
  public int getSize() {
    return mPlanets.size();
  }
}
