package org.jgalaxy.planets;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;
import org.jgalaxy.ITimeProgression;
import org.jgalaxy.units.IJG_UnitDesign;

import java.time.Duration;

public interface IJG_Planet extends IEntity,ITimeProgression, IStorage {

  void rename( String pNewName );

  IJG_Position position();

  double population();
  void setPopulation(double pPopulation);

  double industry();
  void setIndustry( double pIndustry );

  double captitals();
  void setCapitals( double pCapitals );

  double cols();
  void setCols(double pCols);

  double size();
  void setSize( double pSize );

  void setProduceType( EProduceType pType, IJG_UnitDesign pDesign );

}
