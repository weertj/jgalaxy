package org.jgalaxy.engine;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.jgalaxy.Entity;
import org.jgalaxy.OrderException;
import org.jgalaxy.ai.IAI_Faction;
import org.jgalaxy.common.IC_Message;
import org.jgalaxy.los.FLOS_Visibility;
import org.jgalaxy.orders.EJG_Order;
import org.jgalaxy.orders.IJG_Order;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.SJG_OrderExecutor;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.planets.JG_Planet;
import org.jgalaxy.planets.JG_Planets;
import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.tech.JG_Tech;
import org.jgalaxy.units.*;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JG_Faction extends Entity implements IJG_Faction {

  static public File getFactionDirectory( IJG_GameInfo pGameInfo, IJG_Faction pFaction ) {
    File dir = new File( pGameInfo.getGameDir(), "/factions/" + pFaction.id() );
//    File dir = new File( pBaseDir, "games/" + pGameInfo.id() + "/factions/" + pFaction.id() );
    return dir;
  }

  static public IJG_Faction of(IJG_Game pGame, Node pParent ) {
    String id = XML_Utils.attr(pParent,"id");
    String name = XML_Utils.attr(pParent,"name");
    IJG_Faction faction = of(pGame,id,name);

    faction.tech().copyOf(JG_Tech.of(pParent));

    faction.setReconTotalPop( Double.parseDouble(XML_Utils.attr(pParent,"totalPop", "-1")));
    faction.setReconTotalIndustry( Double.parseDouble(XML_Utils.attr(pParent,"totalIndustry", "-1")));

    String atWarWith = XML_Utils.attr(pParent,"atWarWith");
    Arrays.stream(atWarWith.split("\\|")).forEach(faction::addWarWith);

    for(Element ud : XML_Utils.childElementsByName(pParent,"unitdesign")) {
      faction.addUnitDesign(JG_UnitDesign.of(ud));
    }
    for(Element ud : XML_Utils.childElementsByName(pParent,"planet")) {
//      var p = pGame.galaxy().map().planets().findPlanetById(XML_Utils.attr(ud, "id"));
      IJG_Planet p = JG_Planet.of(ud);
      faction.planets().addPlanet(p);
    }
    for(Element ud : XML_Utils.childElementsByName(pParent,"group")) {
      IJG_Group group = JG_Group.of(ud);
      group.setFaction(faction.id());
      faction.groups().addGroup( group );
    }
    for(Element ud : XML_Utils.childElementsByName(pParent,"fleet")) {
      faction.groups().addFleet( XML_Utils.attr(ud,"id"),XML_Utils.attr(ud,"name") );
    }
    faction.setCurrentGroupCounter( Integer.parseInt(XML_Utils.attr(pParent,"currentGroupCounter", "0")) );

    for(Element ud : XML_Utils.childElementsByName(pParent,"incoming")) {
      IJG_Incoming incoming = JG_Incoming.of(ud);
      faction.addIncoming(incoming);
    }

    for(Element ud : XML_Utils.childElementsByName(pParent,"bombings")) {
      IJG_Bombing bombing = JG_Bombing.of(ud);
      faction.addBombing(bombing);
    }

    for( Element ofact : XML_Utils.childElementsByName(pParent,"otherfaction")) {
      IJG_Faction ofaction = of(pGame,ofact );
      faction.getOtherFactionsMutable().add(ofaction);
    }

    var ai = XML_Utils.attr(pParent,"ai", null);
    if (ai!=null) {
      try {
        faction.setAI((IAI_Faction)Class.forName(ai).getConstructor().newInstance());
      } catch (Throwable e) {
        e.printStackTrace();
      }
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
  private final AtomicInteger         mCurrentGroupCount = new AtomicInteger(0);

  private final List<IJG_Incoming>    mIncomings = new ArrayList<>(8);
  private final List<IJG_Bombing>     mBombings = new ArrayList<>(8);

  private final List<IJG_Faction>     mOtherFactions = new ArrayList<>(8);

  private final List<IC_Message>      mMessages = new ArrayList<>(8);

  private final IntegerProperty       mChangeCounter = new SimpleIntegerProperty(0);

  private       double                mReconTotalPop;
  private       double                mReconTotalIndustry;

  private       IAI_Faction           mAIFaction;
  private       IJG_Orders            mOrders;

  private final transient List<OrderException> mOrderErrors = new ArrayList<>(8);

  private JG_Faction( IJG_Game pGame, String pID, String pName ) {
    super(pID,pName);
    mGame = pGame;
    return;
  }

  @Override
  public void close() {
    return;
  }

  @Override
  public IAI_Faction getAI() {
    return mAIFaction;
  }

  @Override
  public void setAI(IAI_Faction pAI) {
    mAIFaction = pAI;
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
    if (!mUnitDesigns.contains(pDesign)) {
      mUnitDesigns.add(pDesign);
    }
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
  public void doOrders( EPhase pPhase ) {
    if (mOrders!=null) {
      switch (pPhase) {
//        case 1 -> {
//          for (var order : mOrders.ordersBy(EJG_Order.DESIGN,EJG_Order.PRODUCE, EJG_Order.SEND, EJG_Order.WAR)) {
//            try {
//              SJG_OrderExecutor.exec(this, order, mGame);
//            } catch (OrderException e) {
//              mOrderErrors.add(e);
//            }
//          }
//        }
        case DESIGN -> defaultDoOrders(EJG_Order.DESIGN );
        case JOIN -> defaultDoOrders(EJG_Order.JOIN );
        case PLANET_PRODUCTION -> defaultDoOrders(EJG_Order.PRODUCE );
        case SEND -> defaultDoOrders(EJG_Order.SEND );
        case DECLAREALLIANCE -> defaultDoOrders(EJG_Order.ALLIANCE );
        case DECLAREWAR -> defaultDoOrders(EJG_Order.WAR );
        case LOAD -> defaultDoOrders(EJG_Order.LOAD );
        case UNLOAD -> defaultDoOrders(EJG_Order.UNLOAD );
        case RENAME -> defaultDoOrders(EJG_Order.RENAME, EJG_Order.CHANGERACENAME );
        case ROUNDUP -> defaultDoOrders(EJG_Order.ELIMINATE, EJG_Order.MESSAGE );
      }
    }
    return;
  }

  private void defaultDoOrders( EJG_Order... pOrder ) {
    for (var order : mOrders.ordersBy(pOrder )) {
      try {
        switch (order.order()) {
          case CHANGERACENAME -> SJG_OrderExecutor.orderCHANGERACENAME(mGame,this,order);
          case DESIGN -> SJG_OrderExecutor.orderDESIGN(mGame,this,order);
          case ELIMINATE -> SJG_OrderExecutor.orderELIMINATE(mGame,this,order);
          case JOIN -> SJG_OrderExecutor.orderJOIN(mGame,this,order);
          case PRODUCE -> SJG_OrderExecutor.orderPRODUCE(mGame,this,order);
          case SEND -> SJG_OrderExecutor.orderSEND(mGame,this,order);
          case ALLIANCE -> SJG_OrderExecutor.orderALLIANCE(mGame,this,order);
          case WAR -> SJG_OrderExecutor.orderWAR(mGame,this,order);
          case LOAD -> SJG_OrderExecutor.orderLOAD(mGame,this,order);
          case UNLOAD -> SJG_OrderExecutor.orderUNLOAD(mGame,this,order);
          case RENAME -> SJG_OrderExecutor.orderRENAME(order,mGame);
          case MESSAGE -> SJG_OrderExecutor.orderMESSAGE(mGame,this,order);
        }
//        SJG_OrderExecutor.exec(this, order, mGame);
      } catch (OrderException e) {
        mOrderErrors.add(e);
      }
    }
    return;
  }

  @Override
  public IJG_Groups groups() {
    return mGroups;
  }

  @Override
  public List<OrderException> orderExceptions() {
    return mOrderErrors;
  }

  @Override
  public IJG_Tech tech() {
    return mTech;
  }

  @Override
  public double totalPop() {
    return planets().planetsOwnedBy(this).stream()
      .mapToDouble(IJG_Planet::population)
      .sum();
  }

  @Override
  public double totalIndustry() {
    return planets().planetsOwnedBy(this).stream()
      .mapToDouble(IJG_Planet::industry)
      .sum();
  }

  @Override
  public void setCurrentGroupCounter(int pCurrentGroupCounter) {
    mCurrentGroupCount.set(pCurrentGroupCounter);
    return;
  }

  @Override
  public int currentGroupCounter() {
    return mCurrentGroupCount.get();
  }

  @Override
  public int currentGroupCounterAndIncrement() {
    return mCurrentGroupCount.getAndIncrement();
  }

  @Override
  public List<IJG_Faction> getOtherFactionsMutable() {
    return mOtherFactions;
  }

  @Override
  public List<IJG_Incoming> getIncomingMutable() {
    return mIncomings;
  }

  @Override
  public void addIncoming(IJG_Incoming pIncoming) {
    if (!mIncomings.contains(pIncoming)) {
      mIncomings.add(pIncoming);
    }
    return;
  }

  @Override
  public List<IJG_Bombing> getBombingsMutable() {
    return mBombings;
  }

  @Override
  public void addBombing(IJG_Bombing pBombing) {
    if (!mBombings.contains(pBombing)) {
      mBombings.add(pBombing);
    }
    return;
  }

  @Override
  public List<IC_Message> getMessagesMutable() {
    return mMessages;
  }

  @Override
  public IntegerProperty changeCounterProperty() {
    return mChangeCounter;
  }

  @Override
  public void newChange() {
    synchronized (mChangeCounter) {
      mChangeCounter.setValue(mChangeCounter.get() + 1);
    }
    return;
  }

  @Override
  public IJG_Faction resolveFactionById(String pFactionId) {
    if (Objects.equals(id(), pFactionId)) {
      return this;
    }
    for( IJG_Faction faction : getOtherFactionsMutable()) {
      if (faction.id().equals(pFactionId)) {
        return faction;
      }
    }
    return null;
  }

  @Override
  public void setReconTotalPop(double pReconTotalPop) {
    mReconTotalPop = pReconTotalPop;
    return;
  }

  @Override
  public double getReconTotalPop() {
    return mReconTotalPop;
  }

  @Override
  public void setReconTotalIndustry(double pReconTotalIndustry) {
    mReconTotalIndustry = pReconTotalIndustry;
    return;
  }

  @Override
  public double getReconTotalIndustry() {
    return mReconTotalIndustry;
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
    Element factionnode = doc.createElement( pName.equals("")?"faction":pName );
    boolean isOtherfaction = "otherfaction".equals(pName);
    factionnode.setAttribute("id", id() );
    factionnode.setAttribute("name", name() );
    factionnode.setAttribute("atWarWith", atWarWith().stream().collect(Collectors.joining("|")));
    factionnode.setAttribute("tech.drive", ""+tech().drive());
    factionnode.setAttribute("tech.weapons", ""+tech().weapons());
    factionnode.setAttribute("tech.shields", ""+tech().shields());
    factionnode.setAttribute("tech.cargo", ""+tech().cargo());
    factionnode.setAttribute("totalPop", ""+totalPop() );
    factionnode.setAttribute("totalIndustry", ""+totalIndustry() );
    if (getAI()!=null) {
      factionnode.setAttribute("ai", getAI().getClass().getName());
    }
    if (!isOtherfaction) {
      factionnode.setAttribute("currentGroupCounter", "" + currentGroupCounter());
    }

    for( IJG_UnitDesign ud : mUnitDesigns) {
      ud.storeObject(pPath,factionnode,"", "");
    }
    for( IJG_Planet planet : mPlanets.planets()) {
      planet.storeObject(pPath, factionnode, "", "");
//      double vis = planet.visibilityFor(mGame, this);
//      IJG_Planet cplanet = planet.copyOf();
//      cplanet.setPlanetToVisibility(vis,null);
//      cplanet.storeObject(pPath, factionnode, "", "");
    }
    for( IJG_Group group : mGroups.getGroups()) {
      group.storeObject(pPath,factionnode,"", isOtherfaction?"shots":"");
    }
    for( IJG_Fleet fleet : mGroups.fleets()) {
      fleet.storeObject(pPath,factionnode,"", "");
    }
    if (orders()!=null) {
      orders().storeObject(pPath, factionnode, "", "");
    }
    // **** Other factions
    for( IJG_Faction otherfaction : getOtherFactionsMutable()) {
      otherfaction.storeObject( null,factionnode,"otherfaction", "");
    }

    // **** Incoming groups
    for( IJG_Incoming incoming : getIncomingMutable()) {
      incoming.storeObject( null,factionnode,"", "");
    }
    // **** Bombings
    for( IJG_Bombing bombing : getBombingsMutable()) {
      bombing.storeObject( null,factionnode,"", "");
    }
    // **** Messages
    for( IC_Message message : mMessages) {
      message.storeObject( null,factionnode,"", "");
    }

    root.appendChild(factionnode);

    if (pPath!=null) {
      try {
        File factiondir = new File(pPath, id());
        String factionxml = XML_Utils.documentToString(doc);
        factiondir.mkdirs();
        GEN_Streams.writeStringToFile(factionxml, new File(factiondir, "faction_" + mGame.turnNumber() + ".xml"));
      } catch (IOException | TransformerException e) {
        e.printStackTrace();
      }
    }

    return;
  }

  @Override
  public String toString() {
    return "JG_Faction [id=" + id() + "]";
  }
}
