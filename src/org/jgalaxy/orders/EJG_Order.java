package org.jgalaxy.orders;

public enum EJG_Order {

  MESSAGE( "@" ),
  ALLIANCE( "a" ),
  BREAKOFF( "b" ),
  CHANGERACENAME( "c" ),
  DESIGN( "d" ),
  ELIMINATE( "e" ),
  JOIN( "j"),
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
