package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;

public interface IJG_Bombing extends IStorage {

  String who();
  String whichGroup();
  IJG_Position position();

}
