package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_Groups;
import org.jgalaxy.units.IJG_UnitDesign;

import java.util.List;

public interface IJG_Faction extends IEntity, IStorage {

  List<String> atWarWith();
  void removeWarWith( String pFactionid );
  void addWarWith( String pFactionid );

  void addUnitDesign( IJG_UnitDesign pDesign );
  List<IJG_UnitDesign> unitDesigns();
  default IJG_UnitDesign getUnitDesignById( String pId ) {
    return unitDesigns().stream().filter(ud -> ud.id().equals( pId )).findFirst().orElse( null );
  }

  IJG_Planets planets();

  IJG_Orders orders();
  void setOrders(IJG_Orders pOrders);

  void doOrders( int pPhase );

  IJG_Tech tech();

  IJG_Groups groups();

}
