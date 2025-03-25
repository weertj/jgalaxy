package org.jgalaxy.engine;

import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;


public class JG_GameInfo implements IJG_GameInfo {

  static public IJG_GameInfo of(File pGameDir) throws Exception {
    return new JG_GameInfo(pGameDir.getName(), pGameDir,-1);
  }

  static public IJG_GameInfo of(Node pRoot) throws Exception {
    return new JG_GameInfo(XML_Utils.attr(pRoot,"name"),null, Integer.parseInt(XML_Utils.attr(pRoot,"currentTurnNumber", "-1" )));
  }

  private final File    mGameDir;
  private final String  mName;
  private       int     mCurrentTurnNumber = -1;

  private JG_GameInfo( String pName, File pDir, int pCurrentTurnNumber ) {
    mName = pName;
    mGameDir = pDir;
    mCurrentTurnNumber = pCurrentTurnNumber;
    return;
  }

  @Override
  public String id() {
    return mName;
  }

  @Override
  public String name() {
    return mName;
  }

  @Override
  public void init() {
    mGameDir.mkdirs();
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
    gameinfonode.setAttribute("name", mName );
    gameinfonode.setAttribute("currentTurnNumber", ""+currentTurnNumber() );
    pParent.appendChild(gameinfonode);
    return;
  }

}
