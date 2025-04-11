package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;

import java.io.File;

public interface IJG_GameInfo extends IStorage, IEntity {

  File getGameDir();

  EGameType getGameType();

  void init();

  int firstTurnNumber();

  int currentTurnNumber();

}
