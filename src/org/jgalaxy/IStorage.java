package org.jgalaxy;


import org.w3c.dom.Node;

import java.io.File;

public interface IStorage {

  default void removeTurnNumber( File pPath, long pTurnNumber ) {
    throw new UnsupportedOperationException();
  }
  void storeObject(File pPath, Node pParent, String pName);

}
