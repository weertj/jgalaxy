package org.jgalaxy.engine;

import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;


public class JG_GameInfo implements IJG_GameInfo {

  static public IJG_GameInfo of(File pGameDir) throws Exception {
    return new JG_GameInfo(pGameDir,-1);
  }

  static public IJG_GameInfo of(Node pRoot) throws Exception {
    return new JG_GameInfo(null, Integer.parseInt(XML_Utils.attr(pRoot,"currentTurnNumber", "-1" )));
  }

  private final File mGameDir;
  private       int  mCurrentTurnNumber = -1;

  private JG_GameInfo( File pDir, int pCurrentTurnNumber ) {
    mGameDir = pDir;
    mCurrentTurnNumber = pCurrentTurnNumber;
    return;
  }

  @Override
  public int currentTurnNumber() {
    if (mCurrentTurnNumber<0) {
      int maxTurnNumber = 0;
      for (File f : mGameDir.listFiles()) {
        if (f.getName().startsWith("game_")) {
          maxTurnNumber = Math.max(maxTurnNumber,
            Integer.parseInt(f.getName().substring("game_".length(), f.getName().indexOf('.'))));
        }
      }
      return maxTurnNumber;
    }
    return mCurrentTurnNumber;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter) {
    Document doc = pParent.getOwnerDocument();
    Element gameinfonode = doc.createElement( "game" );
    gameinfonode.setAttribute("currentTurnNumber", ""+currentTurnNumber() );
    pParent.appendChild(gameinfonode);
    return;
  }

}
