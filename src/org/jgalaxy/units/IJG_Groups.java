package org.jgalaxy.units;

import org.jgalaxy.engine.IJG_Faction;

import java.util.List;

public interface IJG_Groups {

  IJG_Group getGroupById( String pGroupId );

  void addGroup( IJG_Group pGroup );

  List<IJG_Group> getGroups();

  void combineGroups();
  void moveGroups(IJG_Faction pFaction);

}
