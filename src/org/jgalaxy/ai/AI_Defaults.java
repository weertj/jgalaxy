package org.jgalaxy.ai;

import java.io.File;
import java.io.IOException;

public class AI_Defaults {

  static public NNetwork SHIPCLASSNN;

  static {
    try {
      SHIPCLASSNN = NNetwork.of(new File("data/training/ShipClass.data"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
