package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;
import org.jgalaxy.planets.IJG_Planet;

import java.util.List;

public interface IJG_Player extends IEntity, IStorage {

  void addFaction(IJG_Faction faction);
  List<IJG_Faction> factions();
  IJG_Faction getFactionByID(String factionID);

  List<IJG_Planet> planets();

}
