package org.jgalaxy.ai;

import org.jgalaxy.Global;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.IJG_GameInfo;
import org.jgalaxy.engine.JG_Faction;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.JG_Orders;
import org.jgalaxy.planets.EProduceType;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.JG_Planet;
import org.jgalaxy.units.IJG_UnitDesigns;
import org.jgalaxy.units.JG_UnitDesign;
import org.jgalaxy.units.JG_UnitDesigns;
import org.jgalaxy.utils.GEN_Math;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;

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

    colonizer();

//    for(IJG_Planet planet : mFaction.planets().planetsOwnedBy(mFaction)) {
//      planet.setProduceType(EProduceType.PR_DRIVE, null);
//    }

    for( IJG_Planet ownplanet : mFaction.planets().planetsOwnedBy(mFaction)) {
      if (ownplanet.populationFull()) {
        IJG_UnitDesigns unitDesigns = JG_UnitDesigns.of(mFaction.unitDesigns());
        var colShips = unitDesigns.byPrefix("col");
        var colShip = colShips.get(GEN_Math.math().randomRange(0,colShips.size()-1));
        ownplanet.setProduceType(EProduceType.PR_SHIP, colShip.id());
      }
    }

    return;
  }

  private void colonizer() {
    mFaction.addUnitDesign(JG_UnitDesign.of("col"+Global.uniqueID(), "colrecon", 10,3,1,3,5 ));
    mFaction.addUnitDesign(JG_UnitDesign.of("col"+Global.uniqueID(), "col1", 10,0,0,0,10 ));
    mFaction.addUnitDesign(JG_UnitDesign.of("col"+Global.uniqueID(), "col2", 20,0,0,0,20 ));

    return;
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
