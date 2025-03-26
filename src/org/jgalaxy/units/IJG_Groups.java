package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;

import java.util.List;

public interface IJG_Groups {

  default boolean isEmpty() {
    return getGroups().isEmpty();
  }

  IJG_Group getGroupById( String pGroupId );

  void addGroup( IJG_Group pGroup );
  default void addGroups( IJG_Groups pGroups ) {
    pGroups.getGroups().stream().forEach( this::addGroup );
    return;
  }
  default void addGroups( List<IJG_Group> pGroups ) {
    pGroups.stream().forEach( this::addGroup );
    return;
  }

  List<IJG_Group> getGroups();

  IJG_Groups groupsByPosition(IJG_Position pPosition);
  IJG_Groups groupsByFaction(IJG_Faction pFaction);
  IJG_Groups groupsByFactions( List<String> pFactions );

  void combineGroups();
  void moveGroups(IJG_Faction pFaction);

  void removeGroup( IJG_Group pGroup );

  void shuffle();

  int totalNumberOfUnits();

  IJG_Group getGroupByIndex( int pIndex );

  List<IJG_Fleet> fleets();

}
