package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.units.IJG_UnitDesign;
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


  private final String mID;
  private       String mName;
  private IJG_Position mPosition;
  private IJG_Faction  mOwner;
  private       double mSize;
  private       double mResources = 10.0;
  private       double mPopulation;
  private       double mIndustry;
  private       EProduceType mProducing;
  private IJG_UnitDesign mProducingShipType;
  private       double mCapitals;
  private       double mMaterials;
  private       double mCols;
  private       double mInprogress;
  private       double mSpent;

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
  public double industry() {
    return mIndustry;
  }

  @Override
  public void setIndustry(double pIndustry) {
    mIndustry = pIndustry;
    return;
  }

  @Override
  public double captitals() {
    return mCapitals;
  }

  @Override
  public void setCapitals(double pCapitals) {
    mCapitals = pCapitals;
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
  public void setProduceType(EProduceType pType, IJG_UnitDesign pDesign) {
    mProducing = pType;
    mProducingShipType = pDesign;
    return;
  }

  /****f* Phase/convertcap
   * NAME
   *   convertcap -- convert capital into industry.
   * SYNOPSIS
   *   void convertcap(planet *p)
   * SOURCE
   */
  private void convertcap() {
    double indinc;
    indinc = population()-industry();
    if (indinc > captitals()) {
      indinc = captitals();
    }
    setCapitals( captitals()-indinc );
    setIndustry( industry()+indinc );
    return;
  }

  private void producePhase(Duration pDuration) {
    if (mProducing!=null) {
      switch (mProducing) {
        case PR_SHIP -> {
          double industry = industry() * 0.75 + population() * 0.25 - mSpent + mInprogress;
          mSpent = 0.0;
          produceShip(industry);
        }
      }
    }
    return;
  }

  private void produceShip( double pIndustry ) {
    double INDPERSHIP = 10;
    double typeMass = mProducingShipType.mass();
    double indForProduction, indForMaterials;
    if (typeMass>49.5) {
      typeMass -= 0.01; /* Fudge Factor, cause people keep on
                         * building those 99.01 ships and then
                         * complain they get not built */
    }

    int n=(int)(pIndustry/(typeMass*INDPERSHIP))+1;
    int numberOfShips = 0;
    for( ; n>=0; n-- ) {
      indForProduction = n * typeMass * INDPERSHIP;
      indForMaterials  = (n * typeMass * mMaterials)/mResources;
      if (indForMaterials<0) {
        indForMaterials = 0;
      }
      if (indForProduction+indForMaterials<=pIndustry) {
        numberOfShips = n;
        break;
      }
    }
    if (numberOfShips == 0) {
      /* Delay Construction to next turn */
      mInprogress = pIndustry;
    } else {
      indForProduction = numberOfShips * typeMass * INDPERSHIP;
      indForMaterials = (numberOfShips * typeMass - mMaterials) / mResources;
      if (indForMaterials < 0) {
        indForMaterials = 0;
        mMaterials -= numberOfShips * typeMass;
      } else {
        mMaterials = 0;
      }
      pIndustry -= indForProduction + indForMaterials;
      mInprogress = pIndustry;
    }
    return;
  }

  private void producePopulation(Duration pDuration) {
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
  public void timeProgression(Duration pDuration) {
    producePhase(pDuration);
    producePopulation(pDuration);
    convertcap();
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName) {
    Element planetnode = pParent.getOwnerDocument().createElement( "planet" );
    planetnode.setAttribute("id", id());
    planetnode.setAttribute("name", name());
    planetnode.setAttribute("x", ""+position().x());
    planetnode.setAttribute("y", ""+position().y());
    if (mOwner!=null) {
      planetnode.setAttribute("owner", mOwner.id());
    }
    planetnode.setAttribute("size", ""+size());
    planetnode.setAttribute("population", ""+population());
    planetnode.setAttribute("cols", ""+cols());

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
