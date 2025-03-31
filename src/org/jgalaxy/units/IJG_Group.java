package org.jgalaxy.units;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.tech.IJG_Tech;

public interface IJG_Group extends IEntity, IStorage {

  void copyOf( IJG_Group pGroup );

  void setFleet(String pFleet);
  String getFleet();

  IJG_Position lastStaticPosition();
  IJG_Position position();
  IJG_Position toPosition();

  double maxSpeed(IJG_Faction pFaction);

  int getNumberOf();
  void  setNumberOf( int pNumber );

  String    unitDesign();
  void      setUnitDesign(String unitDesign);
  IJG_Tech  tech();

  String  from();
  void    setFrom(String from);
  String  to();
  void    setTo(String to);

  String      faction();
  void        setFaction( String faction);

  String loadType();
  double load();

  void setLoadType( String pLoadType );
  void setLoad( double pLoad );

  double totalCargoMass();
  double totalMass( IJG_Faction pFaction );

  IJG_Group breakOffGroup(IJG_Game pGame, int pNumberOf );

}
