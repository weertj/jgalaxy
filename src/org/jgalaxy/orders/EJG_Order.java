package org.jgalaxy.orders;

public enum EJG_Order {

  DESIGN( "d" ),
  LOAD("l"),
  UNLOAD("u"),
  RENAME("n"),
  PRODUCE( "p" ),
  SEND( "s" ),
  WAR("w");


  private final String mOrder;

  EJG_Order( String pOrder ) {
    mOrder = pOrder;
    return;
  }

  public String order() {
    return mOrder;
  }


}
