package org.jgalaxy.orders;

public enum EJG_Order {

  RENAME("n"),
  PRODUCE( "p" );

  private final String mOrder;

  EJG_Order( String pOrder ) {
    mOrder = pOrder;
    return;
  }

  public String order() {
    return mOrder;
  }


}
