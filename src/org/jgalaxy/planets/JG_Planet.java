package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;

import java.time.Duration;

public class JG_Planet implements IJG_Planet {

  static public IJG_Planet of( IJG_Position pPosition ) {
    return new JG_Planet(pPosition);
  }

  private IJG_Position mPosition;

  private double mPopulation;
  private double mPopulationIncreasePerHour = 0.008;

  private JG_Planet( IJG_Position pPosition ) {
    mPosition = pPosition;
    mPopulation = 100;
    return;
  }

  @Override
  public IJG_Position position() {
    return mPosition;
  }

  @Override
  public double population() {
    return mPopulation;
  }

  @Override
  public void setPopulation(double pPopulation) {
    mPopulation = pPopulation;
    return;
  }

  @Override
  public void timeProgression(Duration pDuration) {
    long hours = pDuration.toHours();
    mPopulation += hours*mPopulationIncreasePerHour;
    return;
  }

  @Override
  public String toString() {
    return "JG_Planet{" +
      "mPosition=" + mPosition +
      ", mPopulation=" + mPopulation +
      '}';
  }
}
