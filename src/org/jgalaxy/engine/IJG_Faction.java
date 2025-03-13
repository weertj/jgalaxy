package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.units.IJG_UnitDesign;

import java.util.List;

public interface IJG_Faction extends IEntity, IStorage {

  void addUnitDesign( IJG_UnitDesign pDesign );
  List<IJG_UnitDesign> unitDesigns();

  IJG_Planets planets();
  void addPlanet( IJG_Planet pPlanet );

  IJG_Orders orders();
  void setOrders(IJG_Orders pOrders);

  void doOrders( int pPhase );

}
