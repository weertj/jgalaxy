package org.jgalaxy;

import org.jgalaxy.ai.AI_Defaults;
import org.jgalaxy.ai.NNetwork;

import java.util.concurrent.atomic.AtomicLong;

public class Global {

  static final private AtomicLong ID = new AtomicLong(System.currentTimeMillis());

  static public double INDPERSHIP = 10;

  static public String uniqueID() {
    return "id-" + ID.getAndIncrement();
  }

  static public void init() {
    NNetwork nn = AI_Defaults.SHIPCLASSNN;
  }

  private Global() {
    return;
  }

}
