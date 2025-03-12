package org.jgalaxy;

import org.jgalaxy.map.IMAP_Map;

import java.time.Duration;

public class Galaxy implements IGalaxy {

  static public IGalaxy of( IMAP_Map pMap ) {
    return new Galaxy(pMap);
  }

  private final IMAP_Map mMap;

  private Galaxy( IMAP_Map pMap ) {
    mMap = pMap;
    return;
  }

  @Override
  public IMAP_Map map() {
    return mMap;
  }

  @Override
  public void timeProgression(Duration pTimeStep) {
    mMap.timeProgression(pTimeStep);
    return;
  }

}
