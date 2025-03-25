package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;

public interface IJG_GameInfo extends IStorage, IEntity {

  void init();

  int currentTurnNumber();

}
