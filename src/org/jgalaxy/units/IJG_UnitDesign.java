package org.jgalaxy.units;

import org.jgalaxy.IEntity;

public interface IJG_UnitDesign extends IEntity {

  double drive();
  double weapons();
  int    nrweapons();
  double shields();
  double cargo();

  double mass();

}
