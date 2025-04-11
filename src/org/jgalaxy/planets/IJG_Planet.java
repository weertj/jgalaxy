package org.jgalaxy.planets;

import org.jgalaxy.*;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.IJG_Player;

import java.util.Locale;

public interface IJG_Planet extends IFactionOwner,IEntity,ITimeProgression, IStorage, IJG_Position {

  IJG_Planet copyOf();

  void anonymize();

  IJG_Position position();

  default boolean populationFull() {
    return population()>=size();
  }

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
  void setResources( double pResources );

  EProduceType produceType();
  String produceUnitDesign();

  double inProgress();
  void setInProgress( double pInProgress );

  double materials();
  void setMaterials( double pMaterials );

  double spent();
  void setSpent( double pSpent );

  double populationPerCol();
  void setPopulationPerCol( double pPopulationPerCol );

  double populationIncreasePerHour();
  void setPopulationIncreasePerHour( double pPopulationIncreasePerHour );

  double visibilityFor(IJG_Game pGame, IJG_Player   pPlayer);
  double visibilityFor(IJG_Game pGame, IJG_Faction  pFaction);

  void setPlanetToVisibility( double pVisibility, IJG_Planet pRealPlanet );

  default String formatString() {
    return String.format( Locale.US, "%-12s %9.2f %9.2f %9.2f %9.2f",
      name(),
      position().x(), position().y(),
      size(),
      population()
      );
  }

  @Override default double x() { return position().x(); }
  @Override default double y() { return position().y(); }
  @Override default void setX(double x) { position().setX(x); }
  @Override default void setY(double y) { position().setY(y); }

}
