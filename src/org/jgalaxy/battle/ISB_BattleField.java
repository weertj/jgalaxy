package org.jgalaxy.battle;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_Groups;

public interface ISB_BattleField {

  IJG_Position getPosition();

  boolean isBattle();

  void addEntry(IJG_Faction pFaction, IJG_Group pGroup);
  void addEntry(IJG_Faction pFaction, IJG_Groups pGroups);

  boolean battleRound();

}
