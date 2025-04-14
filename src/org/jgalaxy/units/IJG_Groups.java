package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;

import java.util.List;

public interface IJG_Groups {

  default boolean isEmpty() {
    return getGroups().isEmpty();
  }

  int getSize();

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

  boolean isGroupAtPosition(IJG_Position pPosition );
  IJG_Groups groupsByPosition(IJG_Position pPosition);
  IJG_Groups groupsByFaction(IJG_Faction pFaction);
  IJG_Groups groupsByFactions( List<String> pFactions );

  void combineGroups();
  void moveGroups(IJG_Game pGame, IJG_Faction pFaction);

  void removeGroup( IJG_Group pGroup );

  void shuffle();

  int totalNumberOfUnits();

  IJG_Group getGroupByGroupIndex( int pIndex );


  IJG_Group getGroupByIndex( int pIndex );

  IJG_Fleet addFleet( String pID, String pName );
  IJG_Fleet getFleetByName( String pName );
  List<IJG_Fleet> fleets();
  List<IJG_Fleet> fleetsByPosition(IJG_Position pPosition);

}
