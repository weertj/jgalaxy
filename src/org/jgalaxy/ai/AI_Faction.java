package org.jgalaxy.ai;

import org.jgalaxy.Global;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.IJG_GameInfo;
import org.jgalaxy.engine.JG_Faction;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.JG_Orders;
import org.jgalaxy.orders.SJG_LoadOrder;
import org.jgalaxy.planets.EProduceType;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.JG_Planet;
import org.jgalaxy.planets.JG_Planets;
import org.jgalaxy.tech.JG_Tech;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_UnitDesigns;
import org.jgalaxy.units.JG_UnitDesign;
import org.jgalaxy.units.JG_UnitDesigns;
import org.jgalaxy.utils.GEN_Math;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.util.Objects;

public class AI_Faction implements IAI_Faction {

  private IJG_GameInfo  mGameInfo;
  private IJG_Game      mGame;
  private IJG_Faction   mOrigFaction;
  private IJG_Faction   mFaction;

  public AI_Faction() {
    return;
  }

  @Override
  public void createOrders(IJG_GameInfo pGameInfo, IJG_Game pGame, IJG_Faction pOrigFaction, IJG_Faction pFaction) {
    mGameInfo = pGameInfo;
    mGame = pGame;
    mOrigFaction = pOrigFaction;
    mFaction = pFaction;

    for( var faction : mGame.factions() ) {
      if (!faction.id().equals(mFaction.id())) {
        mFaction.addWarWith(faction.id());
      }
    }

    reconer();
    colonizer();

//    for(IJG_Planet planet : mFaction.planets().planetsOwnedBy(mFaction)) {
//      planet.setProduceType(EProduceType.PR_DRIVE, null);
//    }

    for( IJG_Planet ownplanet : mFaction.planets().planetsOwnedBy(mFaction)) {
      if (ownplanet.populationFull()) {
        IJG_UnitDesigns unitDesigns = JG_UnitDesigns.of(mFaction.unitDesigns());
        if (GEN_Math.math().nextRandom()>0.5) {
          var recShips = unitDesigns.byPrefix("rec-");
          var recShip = recShips.get(GEN_Math.math().randomRange(0, recShips.size() - 1));
          ownplanet.setProduceType(EProduceType.PR_SHIP, recShip.id());
        } else if (GEN_Math.math().nextRandom()>0.5) {
          var colShips = unitDesigns.byPrefix("col-");
          var colShip = colShips.get(GEN_Math.math().randomRange(0, colShips.size() - 1));
          ownplanet.setProduceType(EProduceType.PR_SHIP, colShip.id());
        } else {
          ownplanet.setProduceType(EProduceType.PR_CAP,null);
        }
      }
    }

    sendReconShips();
    sendColShips();

    return;
  }

  private void reconer() {
    mFaction.addUnitDesign(JG_UnitDesign.of("rec-"+Global.uniqueID(), "recon1", 4,1,1,2,0 ));
    mFaction.addUnitDesign(JG_UnitDesign.of("rec-"+Global.uniqueID(), "recon2", 8,3,1,4,0 ));
    return;
  }

  private void colonizer() {
    mFaction.addUnitDesign(JG_UnitDesign.of("col-"+Global.uniqueID(), "colrecon", 10,3,1,3,5 ));
    mFaction.addUnitDesign(JG_UnitDesign.of("col-"+Global.uniqueID(), "col1", 10,0,0,0,10 ));
    mFaction.addUnitDesign(JG_UnitDesign.of("col-"+Global.uniqueID(), "col2", 20,0,0,0,20 ));
    return;
  }

  private void sendReconShips() {
    for( IJG_Planet ownplanet : mFaction.planets().planetsOwnedBy(mFaction)) {
      if (ownplanet.cols()>0) {
        for(IJG_Group group : mFaction.groups().groupsByPosition(ownplanet.position()).getGroups()) {
          var design = mFaction.getUnitDesignById(group.unitDesign());
          if (design.canCarry(group.tech())<=0 && design.drive()>0 && design.mass()<33) {
            var planet = pickReconTargetPlanet(group);
            if (planet != null) {
              group.toPosition().setX(planet.x());
              group.toPosition().setY(planet.y());
            }
          }
        }
      }
    }
    return;
  }


  private void sendColShips() {
    for( IJG_Planet ownplanet : mFaction.planets().planetsOwnedBy(mFaction)) {
      if (ownplanet.cols()>0) {
        for(IJG_Group group : mFaction.groups().groupsByPosition(ownplanet.position()).getGroups()) {
          var design = mFaction.getUnitDesignById(group.unitDesign());
          if (design.canCarry(group.tech())>0) {
            SJG_LoadOrder.loadOrder(group, design, "COL", ownplanet, 9999999);
            var planet = pickColTargetPlanet(group);
            if (planet != null) {
              group.toPosition().setX(planet.x());
              group.toPosition().setY(planet.y());
            }
          }
        }
      }
    }

    // **** Unload col ships and send back
    for( IJG_Group group : mFaction.groups().getGroups() ) {
      if ("COL".equals(group.loadType())) {
        var planet = mFaction.planets().findPlanetByPosition(group.position());
        if (planet!=null && !planet.populationFull() && (planet.faction()==null || Objects.equals(planet.faction(),mFaction.id()))) {
          SJG_LoadOrder.unloadOrder(mGame,group,planet,9999999);
          planet = pickColSourcePlanet(group);
          if (planet!=null) {
            group.toPosition().setX(planet.x());
            group.toPosition().setY(planet.y());
          }
        }
      }
    }

    return;
  }

  private IJG_Planet pickReconTargetPlanet(IJG_Position pFrom) {
    var planets = new JG_Planets( mFaction.planets().planets() );
    planets.sortByDistanceFrom(pFrom);
    for( var planet : planets.planets() ) {
      if (!Objects.equals(planet.faction(),mFaction.id())) {
        if (mFaction.groups().groupsByPosition(planet.position()).isEmpty()) {
          return planet;
        }
      }
    }
    return null;
  }

  private IJG_Planet pickColTargetPlanet(IJG_Position pFrom) {
    var planets = new JG_Planets( mFaction.planets().planets() );
    planets.sortByDistanceFrom(pFrom);
    for( var planet : planets.planets() ) {
      if (!planet.populationFull() && (Objects.equals(planet.faction(),mFaction.id()) || planet.faction()==null)) {
        return planet;
      }
    }
    return null;
  }

  private IJG_Planet pickColSourcePlanet(IJG_Position pFrom) {
    var planets = new JG_Planets( mFaction.planets().planetsOwnedBy(mFaction) );
    planets.sortByDistanceFrom(pFrom);
    for( var planet : planets.planets() ) {
      if (planet.cols()>0) {
        return planet;
      }
    }
    return null;
  }

  @Override
  public void sendOrders() {
    if (mFaction!=null) {
      IJG_Orders orders = JG_Orders.generateOf( mGame.turnNumber(), mOrigFaction, mFaction );

      try {
        Document doc = XML_Utils.newXMLDocument();
        Node root = doc.createElement("root");
        doc.appendChild(root);
        orders.storeObject(null, root, "", "");
        String result = XML_Utils.documentToString(doc);
        mFaction.setOrders(orders);
        File factionDir = JG_Faction.getFactionDirectory( new File("workdir"), mGameInfo, mFaction );
        GEN_Streams.writeStringToFile( result, new File(factionDir,"orders_" + mGame.turnNumber() + ".xml" ));

      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return;
  }



}
