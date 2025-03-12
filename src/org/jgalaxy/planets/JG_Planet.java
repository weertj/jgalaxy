package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.w3c.dom.Node;

import java.time.Duration;

public class JG_Planet implements IJG_Planet {

  static public IJG_Planet of( Node pParent ) {
    String id = pParent.getAttributes().getNamedItem("id").getNodeValue();
    double x  = Double.parseDouble(pParent.getAttributes().getNamedItem("x").getNodeValue());
    double y  = Double.parseDouble(pParent.getAttributes().getNamedItem("y").getNodeValue());
    return new JG_Planet( id,JG_Position.of(x,y));
  }

  static public IJG_Planet of( String pID, IJG_Position pPosition ) {
    return new JG_Planet( pID,pPosition);
  }

  private IJG_Position mPosition;

  private final String mID;
  private       String mName;
  private       double mPopulation;
  private       double mPopulationIncreasePerHour = 0.008;

  private JG_Planet( String pID, IJG_Position pPosition ) {
    mID = pID;
    mName = pID;
    mPosition = pPosition;
    mPopulation = 100;
    return;
  }

  @Override
  public String id() {
    return mID;
  }

  @Override
  public String name() {
    return mName;
  }

  @Override
  public IJG_Position position() {
    return mPosition;
  }

  @Override
  public void rename(String pNewName) {
    mName = pNewName;
    return;
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
      ", mID='" + mID + '\'' +
      ", mPopulation=" + mPopulation +
      ", mPopulationIncreasePerHour=" + mPopulationIncreasePerHour +
      '}';
  }
}
