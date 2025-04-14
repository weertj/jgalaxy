package org.jgalaxy.units;

import org.jgalaxy.IEntity;
import org.jgalaxy.IFactionOwner;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;
import org.jgalaxy.battle.IB_Shot;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.tech.IJG_Tech;

import java.util.List;

public interface IJG_Group extends IEntity, IStorage, IFactionOwner, IJG_Position {

  String TYPE = "IJG_Group";

  @Override
  default String entityType() {
    return TYPE;
  }

  void copyOf(IJG_Group pGroup );

  void setFleet(String pFleet);
  String getFleet();

  IJG_Position lastStaticPosition();
  IJG_Position position();
  IJG_Position toPosition();

  default @Override void setX(double x) {
    position().setX(x);
  }
  default @Override void setY(double y) {
    position().setY(y);
  }

  default @Override double x() {
    return position().x();
  }
  default @Override double y() {
    return position().y();
  }

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

  String loadType();
  double load();

  void setLoadType( String pLoadType );
  void setLoad( double pLoad );

  double totalCargoMass();
  double totalMass( IJG_Faction pFaction );

  IJG_Group breakOffGroup(IJG_Game pGame, IJG_Faction pThisFaction,String pNewId, int pNumberOf );

  List<IB_Shot> shotsMutable();

}
