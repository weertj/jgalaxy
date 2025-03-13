package org.jgalaxy;


import org.w3c.dom.Node;

import java.io.File;

public interface IStorage {

  void storeObject(File pPath, Node pParent, String pName);

}
