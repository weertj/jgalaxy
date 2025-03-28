package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;

public interface IJG_GameInfo extends IStorage, IEntity {

  EGameType getGameType();

  void init();

  int currentTurnNumber();

}
