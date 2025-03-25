package org.jgalaxy.planets;

public enum EProduceType {

  PR_CAP( "CAP"),
  PR_MAT( "MAT"),
  PR_SHIP( "SHIP"),
  PR_DRIVE("DRIVE"),
  PR_WEAPONS("WEAPONS"),
  PR_SHIELDS("SHIELDS"),
  PR_CARGO( "CARGO");

  private String mOrder;

  EProduceType(String pOrder) {
    mOrder = pOrder;
    return;
  }

  public String order() {
    return mOrder;
  }

}
