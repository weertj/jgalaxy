package org.jgalaxy;

import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.map.IMAP_Map;
import org.w3c.dom.Node;

import java.io.File;
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
  public void timeProgression(IJG_Game pGame, Duration pTimeStep) {
    mMap.timeProgression(pGame, pTimeStep);
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName) {
    mMap.storeObject( pPath, pParent, pName );
    return;
  }

}
