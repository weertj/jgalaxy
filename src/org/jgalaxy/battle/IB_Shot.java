package org.jgalaxy.battle;

public interface IB_Shot {

  enum TYPE {
    SHIP_SHIP,
    SHIP_PLANET
  }

  TYPE type();
  int round();
  String targetID();
  String targetFaction();
  int hits();
}
