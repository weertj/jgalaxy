package org.jgalaxy.engine;

import org.jgalaxy.Entity;
import org.jgalaxy.los.FLOS_Visibility;
import org.jgalaxy.orders.EJG_Order;
import org.jgalaxy.orders.IJG_Order;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.SJG_OrderExecutor;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.planets.JG_Planets;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.tech.JG_Tech;
import org.jgalaxy.units.*;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JG_Faction extends Entity implements IJG_Faction {

  static public IJG_Faction of(IJG_Game pGame, Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Faction faction = of(pGame,id,name);

    String atWarWith = XML_Utils.attr(pParent,"atWarWith");
    Arrays.stream(atWarWith.split("\\|")).forEach(faction::addWarWith);

    for(Element ud : XML_Utils.childElementsByName(pParent,"unitdesign")) {
      faction.addUnitDesign(JG_UnitDesign.of(ud));
    }
    for(Element ud : XML_Utils.childElementsByName(pParent,"planet")) {
      var p = pGame.galaxy().map().planets().findPlanetById(XML_Utils.attr(ud, "id"));
      faction.planets().addPlanet(p);
    }
    for(Element ud : XML_Utils.childElementsByName(pParent,"group")) {
      IJG_Group group = JG_Group.of(ud);
      group.setFaction(faction.id());
      faction.groups().addGroup( group );
    }

    return faction;
  }

  static public IJG_Faction of( IJG_Game pGame, String pID, String pName ) {
    IJG_Faction faction = new JG_Faction( pGame,pID,pName);
    return faction;
  }

  private final IJG_Game              mGame;
  private final List<String>          mAtWarWith = new ArrayList<>(4);
  private final IJG_Tech              mTech = JG_Tech.of();
  private final IJG_Planets           mPlanets = new JG_Planets(List.of());
  private final List<IJG_UnitDesign>  mUnitDesigns = new ArrayList<>(8);
  private final IJG_Groups            mGroups = JG_Groups.of();

  private       IJG_Orders           mOrders;

  private JG_Faction( IJG_Game pGame, String pID, String pName ) {
    super(pID,pName);
    mGame = pGame;
    return;
  }

  @Override
  public List<String> atWarWith() {
    return new ArrayList<>(mAtWarWith);
  }

  @Override
  public void addWarWith(String pFactionid) {
    if (!pFactionid.isBlank()) {
      removeWarWith(pFactionid);
      mAtWarWith.add(pFactionid);
    }
    return;
  }

  @Override
  public void removeWarWith(String pFactionid) {
    mAtWarWith.remove(pFactionid);
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
  public void setOrders(IJG_Orders pOrders) {
    mOrders = pOrders;
    return;
  }

  @Override
  public IJG_Orders orders() {
    return mOrders;
  }

//  orderinfo phase1orders[] = {
//  {"@", &at_order},           /* send message */
//  {"=", &eq_order},           /* FS 1999/12 set real name */
//  {"a", &a_order},            /* alliance */
//  {"b", &b_order},            /* break off ships */
//  {"d", &d_order},            /* design ship */
//  {"e", &e_order},            /* eliminate ship type */
//  {"f", &f_order},            /* get Race's email address */
//  {"h", &h_order},            /* CB-19980923, to recall (halt) a group */
//  {"i", &i_order},            /* intercept */
//  {"j", &j_order},            /* group join fleet */
//  {"l", &l_order},            /* load cargo */
//  {"m", &m_order},            /* change map area */
//  {"o", &o_order},            /* set options */
//  {"p", &p_order},            /* set production */
//  {"q", &q_order},            /* quit */
//  {"r", &r_order},            /* set route */
//  {"s", &s_order},            /* send group/fleet to planet */
//  {"u", &u_order},            /* unload cargo */
//  {"v", &v_order},            /* claim victory */
//  {"w", &w_order},            /* cancel alliance */
//  {"x", &x_order},            /* scrap group */
//  {"y", &y_order},            /* change password */
//  {"z", &z_order},            /* change email */
//  {NULL, NULL}
//};

  @Override
  public void doOrders(int pPhase) {
    if (mOrders!=null) {
      switch (pPhase) {
        case 1 -> {
          for (var order : mOrders.ordersBy(EJG_Order.PRODUCE)) { SJG_OrderExecutor.exec(this, order, mGame);          }
          for (var order : mOrders.ordersBy(EJG_Order.SEND))    { SJG_OrderExecutor.exec(this, order, mGame);          }
          for (var order : mOrders.ordersBy(EJG_Order.WAR))     { SJG_OrderExecutor.exec(this, order, mGame);          }
        }
        case 2 -> {
          for (var order : mOrders.ordersBy(EJG_Order.LOAD))    { SJG_OrderExecutor.exec(this, order, mGame);          }
        }
        case 3 -> {
          for (var order : mOrders.ordersBy(EJG_Order.UNLOAD))  { SJG_OrderExecutor.exec(this, order, mGame);          }
        }
      }
    }
    return;
  }

  @Override
  public IJG_Groups groups() {
    return mGroups;
  }

  @Override
  public IJG_Tech tech() {
    return mTech;
  }

  @Override
  public void removeTurnNumber(File pPath, long pTurnNumber) {
    File factiondir = new File(pPath,id());
    File f = new File(factiondir, "faction_" + pTurnNumber + ".xml");
    f.delete();
    f = new File(factiondir, "orders_" + pTurnNumber + ".xml");
    f.delete();
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Document doc;
    Node root;
    if (pPath==null) {
      root = pParent;
      doc = pParent.getOwnerDocument();
    } else {
      doc = XML_Utils.newXMLDocument();
      root = doc.createElement("root");
      doc.appendChild(root);
    }
    Element factionnode = doc.createElement( "faction" );
    factionnode.setAttribute("id", id() );
    factionnode.setAttribute("name", name() );
    factionnode.setAttribute( "atWarWith", atWarWith().stream().collect(Collectors.joining("|")));

    for( IJG_UnitDesign ud : mUnitDesigns) {
      ud.storeObject(pPath,factionnode,"", "");
    }
    for( IJG_Planet planet : mPlanets.planets()) {
      double vis = planet.visibilityFor(mGame, this);
      IJG_Planet cplanet = planet.copyOf();
      cplanet.setPlanetToVisibility(vis);
      cplanet.storeObject(pPath, factionnode, "", "");
    }
    for( IJG_Group group : mGroups.getGroups()) {
      group.storeObject(pPath,factionnode,"", "");
    }

    root.appendChild(factionnode);
    if (pPath!=null) {
      try {
        File factiondir = new File(pPath, id());
        String factionxml = XML_Utils.documentToString(doc);
        GEN_Streams.writeStringToFile(factionxml, new File(factiondir, "faction_" + mGame.turnNumber() + ".xml"));
      } catch (IOException | TransformerException e) {
        e.printStackTrace();
      }
    }
  }

}
