package org.jgalaxy.orders;

import org.jgalaxy.OrderException;
import org.jgalaxy.common.C_Message;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.planets.EProduceType;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.units.IJG_Fleet;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_UnitDesign;
import org.jgalaxy.units.JG_UnitDesign;

public class SJG_OrderExecutor {

  static private boolean paramIsAvailable( IJG_Order pOrder, int pP ) {
    return pOrder.param(pP)!=null && !pOrder.param(pP).isBlank();
  }

  static public void orderMESSAGE(IJG_Game pGame, IJG_Faction pFaction,IJG_Order pOrder) throws OrderException {
    String factionid = pOrder.param(0 );
    String message = pOrder.param(1 );
    if (message.isBlank()) return;
    if (factionid == null || factionid.isEmpty()) {
      // **** Global message
      pGame.factions().stream()
        .forEach( f -> f.getMessagesMutable().add( new C_Message(true,message) ));
    } else {
      // **** Messsage to faction
      IJG_Faction faction = pGame.getFactionById(factionid);
      if (faction==null) throw new OrderException(null, pOrder, "Faction " + faction + " not found");
      faction.getMessagesMutable().add( new C_Message( false,message));
    }
    return;
  }


  static public void orderCHANGERACENAME(IJG_Game pGame, IJG_Faction pFaction,IJG_Order pOrder) throws OrderException {
    String name = pOrder.param(0 );
    pFaction.setName(name);
    return;
  }

  static public void orderDESIGN( IJG_Game pGame, IJG_Faction pFaction,IJG_Order pOrder) throws OrderException {
    String name = pOrder.param(0 );
    if ("FLEET".equalsIgnoreCase(name)) {
      pFaction.groups().addFleet(pOrder.param(1), pOrder.param(1));
    } else {
      IJG_UnitDesign design = JG_UnitDesign.of( name, name,
        Double.parseDouble(pOrder.param(1)),
        Double.parseDouble(pOrder.param(2)),
        Integer.parseInt(pOrder.param(3)),
        Double.parseDouble(pOrder.param(4)),
        Double.parseDouble(pOrder.param(5 ))
      );
      pFaction.addUnitDesign(design);
    }
    return;
  }
  static public void orderBREAKOFF( IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) throws OrderException {
    IJG_Group group = pFaction.groups().getGroupById(pOrder.param(0));
    if (group!=null) {
      if (paramIsAvailable(pOrder,2)) {
        if (pOrder.param(2).equals("FLEET")) {
          group.setFleet(null);
        }
      }
    }
    return;
  }

  static public void orderJOIN( IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) throws OrderException {
    IJG_Group group = pFaction.groups().getGroupById(pOrder.param(0));
    IJG_Fleet tofleet = pFaction.groups().getFleetByName(pOrder.param(1));
    if (group==null) {
      IJG_Fleet fleet = pFaction.groups().getFleetByName(pOrder.param(0));
      if (fleet!=null && tofleet!=null) {
        fleet.groups().stream().forEach( g->g.setFleet(tofleet.id()) );
      }
    } else if (tofleet!=null) {
      // **** Transfer number of
      if (paramIsAvailable(pOrder,2)) {
        try {
          int nr = Integer.parseInt(pOrder.param(2));
          group = group.breakOffGroup(pGame,pFaction,null,nr);
          pFaction.groups().addGroup(group);
        } catch (NumberFormatException e) {
          throw new OrderException(pFaction, pOrder, pOrder.param(2) + " not a number");
        }
      }
      if (tofleet.position()==null) {
        group.setFleet(pOrder.param(1));
      } else {
        if (group.position().equals(tofleet.position())) {
          group.setFleet(pOrder.param(1));
        } else {
          throw new OrderException(pFaction, pOrder, "Fleet " + tofleet.name() + " not in position");
        }
      }
    }
    return;
  }

  static public void orderPRODUCE(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) {
    String planetid = pOrder.param(0);
    String produce  = pOrder.param(1);
    IJG_Planet planet = pGame.galaxy().map().planets().findPlanetById(planetid);
//    IJG_Planet planet = pFaction.planets().findPlanetById(planetid);
    if (planet == null) {

    } else {
      if (produce.toUpperCase().equals("CAP")) {
        planet.setProduceType( EProduceType.PR_CAP, null );
      } else if (produce.toUpperCase().equals("MAT")) {
        planet.setProduceType( EProduceType.PR_MAT, null );
      } else if (produce.toUpperCase().equals("DRIVE")) {
        planet.setProduceType( EProduceType.PR_DRIVE, null );
      } else if (produce.toUpperCase().equals("WEAPONS")) {
        planet.setProduceType( EProduceType.PR_WEAPONS, null );
      } else if (produce.toUpperCase().equals("SHIELDS")) {
        planet.setProduceType( EProduceType.PR_SHIELDS, null );
      } else if (produce.toUpperCase().equals("CARGO")) {
        planet.setProduceType( EProduceType.PR_CARGO, null );
      } else {
        planet.setProduceType( EProduceType.PR_SHIP, produce );
      }
    }
    return;
  }

  /**
   * orderLOAD
   * @param pOrder
   * @param pGame
   */
  static public void orderLOAD( IJG_Game pGame, IJG_Faction pFaction,IJG_Order pOrder) throws OrderException {
    String groupfleetid = pOrder.param(0 );
    IJG_Group group = pFaction.groups().getGroupById(groupfleetid);
    if (group == null) throw new OrderException(pFaction,pOrder,"Group "+groupfleetid+" not found");
    IJG_UnitDesign unitdesign = pFaction.getUnitDesignById(group.unitDesign());
    if (unitdesign == null) throw new OrderException(pFaction,pOrder,"Unit design "+group.unitDesign()+" not found");
    IJG_Planet planet = pGame.galaxy().map().planets().findPlanetByPosition(group.position());
//    IJG_Planet planet = pFaction.planets().findPlanetByPosition(group.position());
    if (planet == null) throw new OrderException(pFaction,pOrder,"Planet on position "+group.position()+" not found");
    SJG_LoadOrder.loadOrder(group, unitdesign, pOrder.param(1), planet, 9999999 );
    return;
  }

  static public void orderUNLOAD( IJG_Game pGame, IJG_Faction pFaction,IJG_Order pOrder) throws OrderException {
    String groupfleetid = pOrder.param(0 );
    IJG_Group group = pFaction.groups().getGroupById(groupfleetid);
    if (group == null) throw new OrderException(pFaction,pOrder,"Group "+groupfleetid+" not found");
    IJG_Planet planet = pGame.galaxy().map().planets().findPlanetByPosition(group.position());
    if (planet == null) throw new OrderException(pFaction,pOrder,"Planet on position "+group.position()+" not found");
    SJG_LoadOrder.unloadOrder(pGame,group, planet, 9999999 );
    return;
  }

  static public void orderSEND(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) throws OrderException {
    String groupfleetid = pOrder.param(0 );
    String planetid = pOrder.param(1);
    IJG_Planet planet = pGame.galaxy().map().planets().findPlanetById(planetid);
    if (planet == null) throw new OrderException(pFaction, pOrder, "Planet " + planetid + " not found");
    IJG_Fleet fleet = pFaction.groups().getFleetByName(groupfleetid);
    if (fleet==null) {
      IJG_Group group = pFaction.groups().getGroupById(groupfleetid);
      if (group == null) throw new OrderException(pFaction, pOrder, "Group " + groupfleetid + " not found");
      if (pOrder.param(2).isBlank()) {
        group.toPosition().copyOf(planet.position());
      } else {
        var breakgroup = group.breakOffGroup(pGame, pFaction, null, Integer.parseInt(pOrder.param(2)));
        breakgroup.toPosition().copyOf(planet.position());
        if (breakgroup==group) {
        } else {
          pFaction.groups().addGroup(breakgroup);
        }
      }
    } else {
      // **** Move fleet
      fleet.groups().stream().forEach( g->g.toPosition().copyOf(planet.position()) );
    }
    return;
  }

  /**
   * orderWAR
   * @param pGame
   * @param pFaction
   * @param pOrder
   */
  static public void orderWAR(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) {
    String factionid = pOrder.param(0 );
    pFaction.addWarWith(factionid);
    pGame.getFactionById(pFaction.id()).addWarWith(factionid);
    return;
  }

  static public void orderALLIANCE(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) {
    String factionid = pOrder.param(0 );
    pFaction.removeWarWith(factionid);
    pGame.getFactionById(factionid).removeWarWith(pFaction.id());
    return;
  }

  static public void orderELIMINATE(  IJG_Game pGame, IJG_Faction pFaction, IJG_Order pOrder ) {
    String name = pOrder.param(0 );
//    throw new UnsupportedOperationException();
    return;
  }

  /**
   * orderRENAME
   * @param pOrder
   * @param pGame
   */
  static public void orderRENAME( IJG_Order pOrder, IJG_Game pGame ) {
    IJG_Planet planet = pGame.galaxy().map().planets().findPlanetById(pOrder.parameters().get(0) );
    if (planet!=null && pOrder.parameters().size()>1) {
      if (!pOrder.parameters().get(1).isBlank()) {
        planet.setName(pOrder.parameters().get(1));
      }
    }
    return;
  }

  private SJG_OrderExecutor() {
    return;
  }

}
