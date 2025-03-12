package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.ITimeProgression;

import java.time.Duration;

public interface IJG_Planet extends ITimeProgression {

  IJG_Position position();

  double population();

  void setPopulation(double pPopulation);


}
