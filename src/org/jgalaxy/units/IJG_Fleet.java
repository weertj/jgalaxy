package org.jgalaxy.units;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;
import org.jgalaxy.engine.IJG_Faction;

import java.util.List;

public interface IJG_Fleet extends IJG_Group, IEntity, IStorage {

  IJG_Position position();

  List<IJG_Group> groups();

  default boolean containsGroups() {
    return !groups().isEmpty();
  }

  @Override
  double maxSpeed(IJG_Faction pFaction);
}
