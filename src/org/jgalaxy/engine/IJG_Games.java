package org.jgalaxy.engine;

import org.jgalaxy.IStorage;

import java.util.List;

public interface IJG_Games extends IStorage {

  List<IJG_GameInfo> games();

}
