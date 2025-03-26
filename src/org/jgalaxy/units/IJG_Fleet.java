package org.jgalaxy.units;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;

import java.util.List;

public interface IJG_Fleet extends IEntity, IStorage {

  IJG_Position position();

  List<IJG_Group> groups();

}
