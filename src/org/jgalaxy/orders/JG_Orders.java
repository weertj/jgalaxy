package org.jgalaxy.orders;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.units.IJG_Fleet;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_Groups;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JG_Orders implements IJG_Orders {


  static public IJG_Orders generateOf( long pNewTurnnumber, IJG_Faction pFromFaction, IJG_Faction pToFaction) {
    IJG_Orders orders = of(pNewTurnnumber);

    // **** Faction
    if (!Objects.equals(pFromFaction.name(),pToFaction.name())) {
      orders.addOrder(JG_Order.of(EJG_Order.CHANGERACENAME,List.of(pToFaction.name())));
    }
    if (!Objects.deepEquals(pFromFaction.atWarWith(),pToFaction.atWarWith())) {
      for( var warfaction : pToFaction.atWarWith()) {
        orders.addOrder(JG_Order.of(EJG_Order.WAR, List.of(warfaction)));
      }
      var from2 = new ArrayList<>(pFromFaction.atWarWith());
      from2.removeAll(pToFaction.atWarWith());
      for( var peacefaction : from2) {
        orders.addOrder(JG_Order.of(EJG_Order.ALLIANCE, List.of(peacefaction)));
      }
    }

    // **** Ship designs (new)
    for( var design : pToFaction.unitDesigns() ) {
      if (pFromFaction.getUnitDesignById(design.id())==null) {
        orders.addOrder(JG_Order.of(EJG_Order.DESIGN,
          List.of(design.id(),
            ""+design.drive(),
            ""+design.weapons(),
            ""+design.nrweapons(),
            ""+design.shields(),
            ""+design.cargo()
            )));
      }
    }

    // **** Check planets
    IJG_Planets planets = pFromFaction.planets();
    for( int ix=0; ix<planets.getSize(); ix++ ) {
      var p1 = planets.planetByIndex(ix);
      if (Objects.equals(p1.faction(),pFromFaction.id())) {
        var p2 = pToFaction.planets().findPlanetById(p1.id());
        if (!Objects.equals(p1.produceUnitDesign(), p2.produceUnitDesign()) && p2.produceUnitDesign()!=null) {
          orders.addOrder(JG_Order.of(EJG_Order.PRODUCE, List.of(p1.id(), p2.produceUnitDesign())));
        } else if (!Objects.equals(p1.produceType(), p2.produceType())) {
          if (p2.produceType()!=null) {
            orders.addOrder(JG_Order.of(EJG_Order.PRODUCE, List.of(p1.id(), p2.produceType().order())));
          }
        }
        if (!Objects.equals(p1.name(),p2.name())) {
          orders.addOrder(JG_Order.of(EJG_Order.RENAME, List.of(p1.id(), p2.name())));
        }
      }
    }

    // **** Check fleets (new)
    for( var fleet : pToFaction.groups().fleets() ) {
      if (!pFromFaction.groups().fleets().contains(fleet)) {
        orders.addOrder(JG_Order.of(EJG_Order.DESIGN, List.of( "FLEET", fleet.name())));
      }
    }

    // **** Check fleets
    for (IJG_Fleet fleet : pToFaction.groups().fleets()) {
      if (!fleet.groups().isEmpty() && fleet.groups().getFirst().to()!=null) {
        orders.addOrder(JG_Order.of(EJG_Order.SEND, List.of(fleet.id(), fleet.groups().getFirst().to())));
      }
    }

    // **** Check groups
    for (IJG_Group group : pToFaction.groups().getGroups()) {
      if (group.to() != null && group.getFleet()==null) {
        orders.addOrder(JG_Order.of(EJG_Order.SEND, List.of(group.id(), group.to(), ""+group.getNumberOf() )));
      }
    }

    // **** Unload/load/join
    IJG_Groups groups = pFromFaction.groups();
    for( int ix=0; ix<groups.getSize(); ix++ ) {
      var g1 = groups.getGroupByGroupIndex(ix);
      var g2 = pToFaction.groups().getGroupById(g1.id());
      if (g2==null) {

      } else {
        if (!Objects.equals(g2.loadType(),g1.loadType())) {
          if (g1.loadType()!=null) {
            orders.addOrder(JG_Order.of( EJG_Order.UNLOAD, List.of(g1.id())));
          }
          if (g2.loadType()!=null) {
            orders.addOrder(JG_Order.of( EJG_Order.LOAD, List.of(g2.id(),g2.loadType(),""+g2.load())));
          }
        }
        if (!Objects.equals(g2.getFleet(),g1.getFleet())) {
          orders.addOrder(JG_Order.of( EJG_Order.JOIN, List.of(g1.id(),g2.getFleet())));
        }
      }
    }
    // **** Handle the new groups
    for( int ix=groups.getSize(); ix<pToFaction.groups().getSize(); ix++ ) {
      var g2 = pToFaction.groups().getGroupByGroupIndex(ix);
      if (g2.getFleet()!=null) {
        orders.addOrder(JG_Order.of( EJG_Order.JOIN, List.of(g2.id(),g2.getFleet(), ""+g2.getNumberOf())));
      }
    }

    return orders;
  }

  static public IJG_Orders of( long pForTurnNumber, Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Orders orders = of(pForTurnNumber);
    for(Element ud : XML_Utils.childElementsByName(pParent,"order")) {
      orders.addOrder(JG_Order.of(ud));
    }
    return orders;
  }

  static public IJG_Orders of(long pForTurnNumber ) {
    return new JG_Orders(pForTurnNumber);
  }

  private final long            mForTurnNumber;
  private final List<IJG_Order> mOrders = new ArrayList<>(8);

  private JG_Orders( long pForTurnNumber ) {
    mForTurnNumber = pForTurnNumber;
    return;
  }

  @Override
  public void addOrder(IJG_Order order) {
    mOrders.add(order);
    return;
  }

  @Override
  public List<IJG_Order> orders() {
    return mOrders;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter) {
    Document doc = pParent.getOwnerDocument();
    Element orders = doc.createElement( "orders" );
    for( IJG_Order order : mOrders) {
      Element orderNode = doc.createElement( "order" );
      orderNode.setAttribute("order", order.order().name() );
      for( int ix=0; ix<8; ix++ ) {
        if (!order.param(ix).isBlank()) {
          orderNode.setAttribute("param" + ix, order.param(ix));
        }
      }
      orders.appendChild(orderNode);
    }
    pParent.appendChild(orders);
    return;
  }
}
