package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.jgalaxy.orders.EJG_Order;
import org.jgalaxy.orders.IJG_Order;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.SJG_OrderExecutor;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.planets.JG_Planets;
import org.jgalaxy.units.IJG_UnitDesign;
import org.jgalaxy.units.JG_UnitDesign;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JG_Faction extends Entity implements IJG_Faction {

  static public IJG_Faction of(IJG_Game pGame, Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Faction faction = of(pGame,id,name);

    for(Element ud : XML_Utils.childElementsByName(pParent,"unitdesign")) {
      faction.addUnitDesign(JG_UnitDesign.of(ud));
    }
    for(Element ud : XML_Utils.childElementsByName(pParent,"planet")) {
      faction.addPlanet( pGame.galaxy().map().planetById(XML_Utils.attr(ud, "id")));
    }

    return faction;
  }

  static public IJG_Faction of( IJG_Game pGame, String pID, String pName ) {
    IJG_Faction faction = new JG_Faction( pGame,pID,pName);
    return faction;
  }

  private final IJG_Game             mGame;
  private final IJG_Planets          mPlanets = new JG_Planets(List.of());
  private final List<IJG_UnitDesign> mUnitDesigns = new ArrayList<>(8);

  private       IJG_Orders           mOrders;

  private JG_Faction( IJG_Game pGame, String pID, String pName ) {
    super(pID,pName);
    mGame = pGame;
    return;
  }

  @Override
  public void addUnitDesign(IJG_UnitDesign pDesign) {
    mUnitDesigns.add(pDesign);
    return;
  }

  @Override
  public List<IJG_UnitDesign> unitDesigns() {
    return mUnitDesigns;
  }

  @Override
  public IJG_Planets planets() {
    return mPlanets;
  }

  @Override
  public void addPlanet(IJG_Planet pPlanet) {
    mPlanets.addPlanet(pPlanet);
    return;
  }

  @Override
  public void setOrders(IJG_Orders pOrders) {
    mOrders = pOrders;
    return;
  }

  @Override
  public IJG_Orders orders() {
    return mOrders;
  }

  @Override
  public void doOrders(int pPhase) {
    if (mOrders!=null) {
      switch (pPhase) {
        case 1 -> {
          for (var order : mOrders.ordersBy(EJG_Order.PRODUCE)) {
            SJG_OrderExecutor.exec(this, order, mGame);
          }
        }
      }
    }
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName) {
    Document doc = XML_Utils.newXMLDocument();
    var root = doc.createElement("root");
    doc.appendChild(root);
    Element factionnode = doc.createElement( "faction" );
    factionnode.setAttribute("id", id() );
    factionnode.setAttribute("name", name() );

    for( IJG_UnitDesign ud : mUnitDesigns) {
      ud.storeObject(pPath,factionnode,"");
    }
    for( IJG_Planet planet : mPlanets.planets()) {
      planet.storeObject(pPath,factionnode,"");
    }

    root.appendChild(factionnode);
    try {
      File factiondir = new File(pPath,id());
      String factionxml = XML_Utils.documentToString(doc);
      GEN_Streams.writeStringToFile(factionxml, new File(factiondir, "faction_" + mGame.turnNumber() + ".xml"));
    } catch (IOException | TransformerException e ) {
      e.printStackTrace();
    }
  }

}
