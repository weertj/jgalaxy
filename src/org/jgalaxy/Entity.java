package org.jgalaxy;

public class Entity implements IEntity {

  private final String mID;
  private       String mName;

  protected Entity( String pID, String pName ) {
    mID = pID;
    mName = pName;
    return;
  }

  @Override
  public String id() {
    return mID;
  }

  @Override
  public String name() {
    return mName;
  }

}
