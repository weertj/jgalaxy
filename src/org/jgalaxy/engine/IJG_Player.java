package org.jgalaxy.engine;

import org.jgalaxy.IStorage;
import org.jgalaxy.planets.IJG_Planet;

import java.util.List;

public interface IJG_Player extends IStorage {

  void addFaction(IJG_Faction faction);
  List<IJG_Faction> factions();

  List<IJG_Planet> planets();

}
