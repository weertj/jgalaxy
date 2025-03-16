package org.jgalaxy.planets;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.IJG_Player;
import org.jgalaxy.los.FLOS_Visibility;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.JG_Group;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.time.Duration;

public class JG_Planet implements IJG_Planet {

  static public IJG_Planet of( Node pParent ) {
    String id = XML_Utils.attr(pParent, "id" );
    String name = XML_Utils.attr(pParent, "name" );
    double x  = Double.parseDouble(XML_Utils.attr(pParent, "x" ));
    double y  = Double.parseDouble(XML_Utils.attr(pParent, "y" ));
    IJG_Planet planet = new JG_Planet( id, name,JG_Position.of(x,y));

    String owner = XML_Utils.attr(pParent, "owner" );
    if (!owner.isBlank()) {
      planet.setOwner(owner);
      String produceType = XML_Utils.attr(pParent, "produceType" );
      if (!produceType.isBlank()) {
        String produceUnitDesign = XML_Utils.attr(pParent, "produceUnitDesign" );
        planet.setProduceType( EProduceType.valueOf(produceType), produceUnitDesign );
      }
    }
    planet.setSize( Double.parseDouble(XML_Utils.attr(pParent, "size" )));
    planet.setPopulation( Double.parseDouble(XML_Utils.attr(pParent, "population","0" )));
    planet.setIndustry( Double.parseDouble(XML_Utils.attr(pParent, "industry","0" )));
    planet.setCapitals( Double.parseDouble(XML_Utils.attr(pParent, "capitals","0" )));
    planet.setMaterials( Double.parseDouble(XML_Utils.attr(pParent, "materials","0" )));
    planet.setCols( Double.parseDouble(XML_Utils.attr(pParent, "cols","0" )));
    planet.setSpent(Double.parseDouble(XML_Utils.attr(pParent, "spent","0" )));
    planet.setPopulationPerCol( Double.parseDouble(XML_Utils.attr(pParent, "populationPerCol", "8" )));
    planet.setPopulationIncreasePerHour( Double.parseDouble(XML_Utils.attr(pParent, "populationIncreasePerHour", "0.08" )));
    return planet;
  }

  static public IJG_Planet of( String pID, String pName, IJG_Position pPosition ) {
    return new JG_Planet( pID, pName, pPosition );
  }


  private final String mID;
  private       String mName;
  private IJG_Position mPosition;
  private       String mOwner;
  private       double mSize;
  private       double mResources = 10.0;
  private       double mPopulation;
  private       double mIndustry;
  private       EProduceType mProducing;
  private       String mProducingShipType;
  private       double mCapitals;
  private       double mMaterials;
  private       double mCols;
  private       double mInprogress;
  private       double mSpent;

  private       double mPopulationPerCol = 8;
  private       double mPopulationIncreasePerHour = 0.008;


  private JG_Planet( String pID, String pName, IJG_Position pPosition ) {
    mID = pID;
    mName = pName;
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
  public void setOwner(String pOwner) {
    mOwner = pOwner;
    return;
  }

  @Override
  public String owner() {
    return mOwner;
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
  public double capitals() {
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
  public void setProduceType(EProduceType pType, String pDesign) {
    mProducing = pType;
    mProducingShipType = pDesign;
    return;
  }

  @Override
  public double resources() {
    return mResources;
  }

  @Override
  public double materials() {
    return mMaterials;
  }

  @Override
  public double inProgress() {
    return mInprogress;
  }

  @Override
  public void setInProgress(double pInProgress) {
    mInprogress = pInProgress;
    return;
  }

  @Override
  public void setMaterials(double pMaterials) {
    mMaterials = pMaterials;
    return;
  }

  @Override
  public void setSpent(double pSpent) {
    mSpent = pSpent;
    return;
  }

  @Override
  public double spent() {
    return mSpent;
  }

  @Override
  public double populationPerCol() {
    return mPopulationPerCol;
  }

  @Override
  public void setPopulationPerCol(double pPopulationPerCol) {
    mPopulationPerCol = pPopulationPerCol;
    return;
  }

  @Override
  public double populationIncreasePerHour() {
    return mPopulationIncreasePerHour;
  }

  @Override
  public void setPopulationIncreasePerHour(double pPopulationIncreasePerHour) {
    mPopulationIncreasePerHour = pPopulationIncreasePerHour;
    return;
  }

  @Override
  public double visibilityFor(IJG_Game pGame, IJG_Player pPlayer) {
    double vis = FLOS_Visibility.VIS_NOT;
    for( IJG_Faction faction : pPlayer.factions() ) {
      vis = Math.max(vis,visibilityFor(pGame,faction));
    }
    return vis;
  }

  @Override
  public double visibilityFor(IJG_Game pGame, IJG_Faction pFaction) {
    if (pFaction.id().equals(owner())) {
      return FLOS_Visibility.VIS_FULL;
    } else {
      return FLOS_Visibility.VIS_MINIMUM;
    }
  }

  @Override
  public void setPlanetToVisibility(double pVisibility) {
    if (pVisibility>=FLOS_Visibility.VIS_FULL) {
      return;
    }
    mPopulation = -1;
    mCapitals = -1;
    mCols = -1;
    mMaterials = -1;
    mIndustry = -1;
    mSize = -1;
    mPopulationPerCol = -1;
    mInprogress = -1;
    mSpent = -1;
    mPopulationIncreasePerHour = -1;
    mProducingShipType = null;
    mProducing = null;
    mOwner = null;
    return;
  }

  @Override
  public EProduceType produceType() {
    return mProducing;
  }

  @Override
  public String produceUnitDesign() {
    return mProducingShipType;
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
    if (indinc > capitals()) {
      indinc = capitals();
    }
    setCapitals( capitals()-indinc );
    setIndustry( industry()+indinc );
    return;
  }

  private void producePhase( IJG_Game pGame, Duration pDuration) {
    if (mProducing!=null) {
      switch (mProducing) {
        case PR_SHIP -> {
          double industry = industry() * 0.75 + population() * 0.25 - mSpent + mInprogress;
          mSpent = 0.0;
          produceShip(pGame,industry);
        }
      }
    }
    return;
  }

  /****f* Phase/produceShip
   * NAME
   *   produceShip -- try to produce some ships.
   * FUNCTION
   *   Computes based on the amount of industry (points) how many ships
   *   can be build. If this is 1 or more the ships are produces and a
   *   new group with these ships is created. If it is less than 1 the
   *   production is delayed to the next turn.
   * NOTE
   *   Because of MAT the expression to compute the number of ships
   *   that can be produced is a tricky one.
   * SOURCE
   */
  private void produceShip( IJG_Game pGame, double pIndustry ) {
    var owner = pGame.getFactionById(mOwner);
    if (owner!=null) {
      var prodship = owner.getUnitDesignById(mProducingShipType);
      double INDPERSHIP = 10;
      double typeMass = prodship.mass();
      double indForProduction, indForMaterials;
      if (typeMass > 49.5) {
        typeMass -= 0.01; /* Fudge Factor, cause people keep on
         * building those 99.01 ships and then
         * complain they get not built */
      }

      int n = (int) (pIndustry / (typeMass * INDPERSHIP)) + 1;
      int numberOfShips = 0;
      for (; n >= 0; n--) {
        indForProduction = n * typeMass * INDPERSHIP;
        indForMaterials = (n * typeMass - mMaterials) / mResources;
        if (indForMaterials < 0) {
          indForMaterials = 0;
        }
        if (indForProduction + indForMaterials <= pIndustry) {
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

        IJG_Group group = JG_Group.of("group_" + JG_Group.GROUPCOUNTER.getAndIncrement(), "newgroup" );
        group.setNumberOf(numberOfShips);
        group.tech().copyOf(owner.tech());
        group.position().setX(position().x());
        group.position().setY(position().y());
        group.toPosition().setX(position().x());
        group.toPosition().setY(position().y());
        group.setUnitDesign(prodship.id());
        owner.groups().addGroup( group );
      }
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
  public void timeProgression(IJG_Game pGame, Duration pDuration) {
    producePhase(pGame,pDuration);
    producePopulation(pDuration);
    convertcap();
    afterAction();
    return;
  }

  private void afterAction() {
    // **** Check cols
    if (population()<size() && cols()>0) {
      double extrapop = size()-population();
      double extracol = extrapop/mPopulationPerCol;
      if (cols()<extracol) {
        setPopulation(population()+cols()*mPopulationPerCol);
        setCols(0.0);
      } else {
        setPopulation(population()+extrapop);
        setCols(cols()-extracol);
      }
    }
    return;
  }

  @Override
  public IJG_Planet copyOf() {
    IJG_Planet planet = of(id(),name(),position());
    planet.setOwner(mOwner);
    planet.setSize(size());
    planet.setPopulation(population());
    planet.setIndustry(industry());
    planet.setProduceType(produceType(), produceUnitDesign());
    planet.setCapitals(capitals());
    planet.setMaterials(materials());
    planet.setCols(cols());
    planet.setInProgress(inProgress());
    planet.setSpent(spent());
    planet.setPopulationPerCol(populationPerCol());
    planet.setPopulationIncreasePerHour(populationIncreasePerHour());
    return planet;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element planetnode = pParent.getOwnerDocument().createElement( "planet" );
    planetnode.setAttribute("id", id());
    planetnode.setAttribute("name", name());
    planetnode.setAttribute("x", ""+position().x());
    planetnode.setAttribute("y", ""+position().y());
    if (mOwner!=null) {
      planetnode.setAttribute("owner", mOwner);
    }
    if (size()>=0)        planetnode.setAttribute("size", ""+size());
    if (population()>=0)  planetnode.setAttribute("population", ""+population());
    if (industry()>=0)    planetnode.setAttribute("industry", ""+industry());
    if (produceType()!=null) {
      planetnode.setAttribute("produceType", produceType().name() );
    }
    if (produceUnitDesign()!=null) {
      planetnode.setAttribute("produceUnitDesign", produceUnitDesign() );
    }
    if (capitals()>=0)    planetnode.setAttribute("capitals", ""+ capitals());
    if (materials()>=0)   planetnode.setAttribute("materials", ""+materials());
    if (cols()>=0)        planetnode.setAttribute("cols", ""+cols());
    if (inProgress()>=0)  planetnode.setAttribute("inProgress", ""+mInprogress);
    if (spent()>=0)       planetnode.setAttribute("spent", ""+mSpent);
    if (populationPerCol()>=0) planetnode.setAttribute("populationPerCol", ""+mPopulationPerCol);
    if (populationIncreasePerHour()>=0) planetnode.setAttribute("populationIncreasePerHour", ""+mPopulationIncreasePerHour);
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
