package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;

import java.io.File;
import java.util.List;

public interface IJG_GameInfo extends IStorage, IEntity {

  default String getName() {
    return name();
  }

  File getGameDir();

  void init();

  int firstTurnNumber();

  int currentTurnNumber();

  List<String> factions();

  List<IJG_Player> players();

}
