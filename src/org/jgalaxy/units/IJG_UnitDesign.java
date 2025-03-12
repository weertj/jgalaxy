package org.jgalaxy.units;

import org.jgalaxy.tech.IJG_Tech;

public interface IJG_UnitDesign {

  double drive();
  double weapons();
  int    nrweapons();
  double shields();
  double cargo();

  IJG_Tech tech();

  double mass();

}
