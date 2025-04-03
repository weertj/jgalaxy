package org.jgalaxy.units;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;
import org.jgalaxy.tech.IJG_Tech;

public interface IJG_UnitDesign extends IEntity, IStorage {

  double drive();
  double weapons();
  int    nrweapons();
  double shields();
  double cargo();

  double mass();

  double speed(IJG_Tech pTech, double pCargoMass);

  double canCarry(IJG_Tech pTech);

  default double effectiveShield( IJG_Tech pTech ) {
    return (pTech.shields() * shields() / Math.pow(mass(),0.3333333)) * 3.10723250595;
  }

  default double effectiveWeapon( IJG_Tech pTech ) {
    return pTech.weapons() * weapons();
  }

}

