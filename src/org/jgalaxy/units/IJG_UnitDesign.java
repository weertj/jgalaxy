package org.jgalaxy.units;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;

public interface IJG_UnitDesign extends IEntity, IStorage {

  double drive();
  double weapons();
  int    nrweapons();
  double shields();
  double cargo();

  double mass();

}
