package org.jgalaxy.planets;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;
import org.jgalaxy.ITimeProgression;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.units.IJG_UnitDesign;

public interface IJG_Planet extends IEntity,ITimeProgression, IStorage {

  void rename( String pNewName );

  IJG_Position position();

  String owner();
  void setOwner( String pOwner );

  double population();
  void setPopulation(double pPopulation);

  double industry();
  void setIndustry( double pIndustry );

  double capitals();
  void setCapitals( double pCapitals );

  double cols();
  void setCols(double pCols);

  double size();
  void setSize( double pSize );

  void setProduceType( EProduceType pType, String pDesign );

  double resources();
  EProduceType produceType();
  String produceUnitDesign();

  double materials();
  void setMaterials( double pMaterials );

  double spent();
  void setSpent( double pSpent );

  double populationPerCol();
  void setPopulationPerCol( double pPopulationPerCol );

  double populationIncreasePerHour();
  void setPopulationIncreasePerHour( double pPopulationIncreasePerHour );

}
