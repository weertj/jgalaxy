package org.jgalaxy.battle;

import org.jgalaxy.IEntity;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.units.IJG_Group;

import java.io.Serializable;
import java.util.List;

public interface ISB_BattleReport extends IEntity {

  boolean isInvolved(IJG_Faction pFaction);

  List<IJG_Group> groups();

}
