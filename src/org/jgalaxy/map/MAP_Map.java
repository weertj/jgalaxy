package org.jgalaxy.map;

import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;
import org.jgalaxy.planets.JG_Planet;
import org.jgalaxy.planets.JG_Planets;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MAP_Map implements IMAP_Map {

  static public IMAP_Map of(IJG_Game pGame, Node pParent ) {
    IMAP_Map map = null;
    double xs = Double.parseDouble(pParent.getAttributes().getNamedItem("xs").getNodeValue());
    double ys = Double.parseDouble(pParent.getAttributes().getNamedItem("ys").getNodeValue());
    double xe = Double.parseDouble(pParent.getAttributes().getNamedItem("xe").getNodeValue());
    double ye = Double.parseDouble(pParent.getAttributes().getNamedItem("ye").getNodeValue());
    map = of( xs, ys, xe, ye );
    for(Element xmlpl : XML_Utils.childElementsByName(pParent,"planet")) {
      map.planets().addPlanet(JG_Planet.of( xmlpl ));
    }
    return map;
  }

  static public IMAP_Map of( double pXStart, double pYStart, double pXEnd, double pYEnd ) {
    return new MAP_Map( pXStart, pYStart, pXEnd, pYEnd );
  }

  private final double mXStart;
  private final double mYStart;
  private final double mXEnd;
  private final double mYEnd;

  private final IJG_Planets mPlanets = new JG_Planets(List.of());

  private MAP_Map( double pXStart, double pYStart, double pXEnd, double pYEnd ) {
    mXStart = pXStart;
    mYStart = pYStart;
    mXEnd = pXEnd;
    mYEnd = pYEnd;
    return;
  }

  @Override
  public double xStart() {
    return mXStart;
  }

  @Override
  public double yStart() {
    return mYStart;
  }

  @Override
  public double xEnd() {
    return mXEnd;
  }

  @Override
  public double yEnd() {
    return mYEnd;
  }


  @Override
  public IJG_Planets planets() {
    return mPlanets;
  }

  @Override
  public void timeProgression(IJG_Game pGame, Duration pTimeStep) {
    return;
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element mapnode = pParent.getOwnerDocument().createElement( "map" );
    mapnode.setAttribute("xs", String.valueOf(mXStart));
    mapnode.setAttribute("ys", String.valueOf(mYStart));
    mapnode.setAttribute("xe", String.valueOf(mXEnd));
    mapnode.setAttribute("ye", String.valueOf(mYEnd));
    for( var planet : planets().planets() ) {
      planet.storeObject(pPath, mapnode, pName,pFilter);
    }
    pParent.appendChild(mapnode);
    return;
  }

}
