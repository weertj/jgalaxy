package org.jgalaxy;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.orders.IJG_Order;

public class OrderException extends Exception {

  private final IJG_Faction mFaction;
  private final IJG_Order   mOrder;

  public OrderException(IJG_Faction pFaction, IJG_Order pOrder, String pMessage) {
    super(pMessage);
    mFaction = pFaction;
    mOrder = pOrder;
  }

  public IJG_Faction getFaction() {
    return mFaction;
  }

  public IJG_Order getOrder() {
    return mOrder;
  }
}
