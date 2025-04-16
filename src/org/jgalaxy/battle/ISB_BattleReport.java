package org.jgalaxy.battle;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.units.IJG_Group;

import java.util.List;

public interface ISB_BattleReport extends IEntity {

  IJG_Position position();

  boolean isInvolved(IJG_Faction pFaction);

  List<IJG_Group> groups();

  int hitsOnGroupForGroup( IJG_Group pTargetGroup, IJG_Group pSourceGroup );
  int calcNumberOfBeforeBattle( IJG_Group pGroup);

}
