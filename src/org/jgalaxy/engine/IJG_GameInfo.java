package org.jgalaxy.engine;

import javafx.scene.image.Image;
import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface IJG_GameInfo extends IStorage, IEntity {

  File getGameDir();

  EGameType getGameType();

  void init();

  int firstTurnNumber();

  int currentTurnNumber();

  List<String> factions();

}
