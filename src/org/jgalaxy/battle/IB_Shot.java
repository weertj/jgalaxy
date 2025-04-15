package org.jgalaxy.battle;

public interface IB_Shot {

  enum TYPE {
    SHIP_SHIP,
    SHIP_SHIP_INCOMING,
    SHIP_PLANET
  }

  enum RESULT {
    NO_CHANCE,
    SHIELDS,
    DESTROYED,
    ALL_DESTROYED,
  }

  TYPE type();
  RESULT result();
  int round();
  String targetID();
  String targetFaction();
  int hits();
}
