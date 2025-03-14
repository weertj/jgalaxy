package org.jgalaxy.units;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;
import org.jgalaxy.tech.IJG_Tech;

public interface IJG_Group extends IEntity, IStorage {

  String getFleet();

  IJG_Position position();
  IJG_Position toPosition();

  int numberOf();
  void setNumberOf( int pNumber );

  String unitDesign();
  void setUnitDesign(String unitDesign);
  IJG_Tech tech();

  String from();
  void setFrom(String from);
  String to();
  void setTo(String to);


}
