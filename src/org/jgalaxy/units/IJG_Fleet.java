package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;

import java.util.List;

public interface IJG_Fleet {

  IJG_Position position();

  List<IJG_Unit> units();

}
