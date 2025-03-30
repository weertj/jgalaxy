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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Entity entity = (Entity) o;
    return mID.equals(entity.mID) && mName.equals(entity.mName);
  }

  @Override
  public int hashCode() {
    int result = mID.hashCode();
    result = 31 * result + mName.hashCode();
    return result;
  }
}
