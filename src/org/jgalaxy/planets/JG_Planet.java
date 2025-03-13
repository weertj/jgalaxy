package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.time.Duration;

public class JG_Planet implements IJG_Planet {

  static public IJG_Planet of( Node pParent ) {
    String id = XML_Utils.attr(pParent, "id" );
    double x  = Double.parseDouble(XML_Utils.attr(pParent, "x" ));
    double y  = Double.parseDouble(XML_Utils.attr(pParent, "y" ));
    IJG_Planet planet = new JG_Planet( id,JG_Position.of(x,y));
    planet.setPopulation( Double.parseDouble(XML_Utils.attr(pParent, "population" )));
    planet.setSize( Double.parseDouble(XML_Utils.attr(pParent, "size" )));
    return planet;
  }

  static public IJG_Planet of( String pID, IJG_Position pPosition ) {
    return new JG_Planet( pID,pPosition);
  }

  private IJG_Position mPosition;

  private final String mID;
  private       String mName;
  private       double mSize;
  private       double mPopulation;
  private       double mCols;
  private       double mPopulationPerCol = 8;
  private       double mPopulationIncreasePerHour = 0.008;

  private JG_Planet( String pID, IJG_Position pPosition ) {
    mID = pID;
    mName = pID;
    mPosition = pPosition;
    mPopulation = 0;
    return;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    JG_Planet jgPlanet = (JG_Planet) o;
    return mID.equals(jgPlanet.mID);
  }

  @Override
  public int hashCode() {
    return mID.hashCode();
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
  public double cols() {
    return mCols;
  }

  @Override
  public void setCols(double pCols) {
    mCols = pCols;
    return;
  }

  @Override
  public double size() {
    return mSize;
  }

  @Override
  public void setSize(double pSize) {
    mSize = pSize;
    return;
  }

  @Override
  public void timeProgression(Duration pDuration) {
    long hours = pDuration.toHours();
    double popinc = population() * hours * mPopulationIncreasePerHour;
    double poproom = size() - population();
    if (poproom>popinc) {
      poproom = popinc;
    }
    mPopulation += poproom;
    mCols += (popinc-poproom)/mPopulationPerCol;
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName) {
    Element planetnode = pParent.getOwnerDocument().createElement( "planet" );
    planetnode.setAttribute("id", id());
    planetnode.setAttribute("name", name());
    planetnode.setAttribute("x", ""+position().x());
    planetnode.setAttribute("y", ""+position().y());
    planetnode.setAttribute("population", ""+population());
    planetnode.setAttribute("cols", ""+cols());
    planetnode.setAttribute("size", ""+size());

    pParent.appendChild(planetnode);
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
