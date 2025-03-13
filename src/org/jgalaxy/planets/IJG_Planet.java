package org.jgalaxy.planets;

import org.jgalaxy.IEntity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;
import org.jgalaxy.ITimeProgression;

import java.time.Duration;

public interface IJG_Planet extends IEntity,ITimeProgression, IStorage {

  void rename( String pNewName );

  IJG_Position position();

  double population();
  void setPopulation(double pPopulation);

  double cols();
  void setCols(double pCols);

  double size();
  void setSize( double pSize );

}
