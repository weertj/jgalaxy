package org.jgalaxy.tech;

public interface IJG_Tech {

  double drive();
  double weapons();
  double shields();
  double cargo();

  void setDrive(double drive);
  void setWeapons(double weapons);
  void setShields(double shields);
  void setCargo(double cargo);

  default void copyOf(IJG_Tech other) {
    setDrive(other.drive());
    setWeapons(other.weapons());
    setShields(other.shields());
    setCargo(other.cargo());
    return;
  }

}
