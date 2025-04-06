package org.jgalaxy;

import java.util.concurrent.atomic.AtomicLong;

public class Global {

  static final private AtomicLong ID = new AtomicLong(System.currentTimeMillis());

  static public String uniqueID() {
    return "id-" + ID.getAndIncrement();
  }

  private Global() {
    return;
  }

}
