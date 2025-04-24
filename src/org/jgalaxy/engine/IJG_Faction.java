package org.jgalaxy.engine;

import javafx.beans.property.IntegerProperty;
import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;
import org.jgalaxy.OrderException;
import org.jgalaxy.ai.IAI_Faction;
import org.jgalaxy.common.IC_Message;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.units.*;

import java.util.List;

public interface IJG_Faction extends IEntity, IStorage {

  IJG_Faction copyOf(IJG_Game pGame);

  void setAI(IAI_Faction pAI);
  IAI_Faction getAI();

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

  void doOrders( EPhase pPhase );

  IJG_Tech tech();

  IJG_Groups groups();

  List<OrderException> orderExceptions();

  double totalPop();
  double totalIndustry();

  int currentGroupCounter();
  int currentGroupCounterAndIncrement();
  void setCurrentGroupCounter( int pCurrentGroupCounter );

  void addOtherFaction( IJG_Faction pFaction );
  List<IJG_Faction>   getOtherFactionsMutable();

  List<IJG_Incoming>  getIncomingMutable();
  void addIncoming( IJG_Incoming pIncoming );

  List<IJG_Bombing>  getBombingsMutable();
  void addBombing( IJG_Bombing pBombing );

  List<IC_Message> getMessagesMutable();

  IntegerProperty changeCounterProperty();
  void newChange();

  IJG_Faction resolveFactionById( String pFactionId );

  void setReconTotalPop( double pReconTotalPop );
  double getReconTotalPop();
  void setReconTotalIndustry( double pReconTotalIndustry );
  double getReconTotalIndustry();

}
