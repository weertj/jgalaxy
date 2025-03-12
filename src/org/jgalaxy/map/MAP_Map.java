package org.jgalaxy.map;

import org.jgalaxy.IGalaxy;
import org.jgalaxy.JG_Position;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.JG_Planet;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MAP_Map implements IMAP_Map {

  static public IMAP_Map of( Node pParent ) {
    IMAP_Map map = null;
    double xs = Double.parseDouble(pParent.getAttributes().getNamedItem("xs").getNodeValue());
    double ys = Double.parseDouble(pParent.getAttributes().getNamedItem("ys").getNodeValue());
    double xe = Double.parseDouble(pParent.getAttributes().getNamedItem("xe").getNodeValue());
    double ye = Double.parseDouble(pParent.getAttributes().getNamedItem("ye").getNodeValue());
    map = new MAP_Map( xs, ys, xe, ye );
    for(Element xmlpl : XML_Utils.childElementsByName(pParent,"planet")) {
      map.addPlanet(JG_Planet.of( xmlpl ));
    }
    return map;
  }

  private final double mXStart;
  private final double mYStart;
  private final double mXEnd;
  private final double mYEnd;

  private final List<IJG_Planet> mPlanets = new ArrayList<>(8);

  private MAP_Map( double pXStart, double pYStart, double pXEnd, double pYEnd ) {
    mXStart = pXStart;
    mYStart = pYStart;
    mXEnd = pXEnd;
    mYEnd = pYEnd;
    return;
  }

  @Override
  public void addPlanet(IJG_Planet pPlanet) {
    mPlanets.add( pPlanet );
    return;
  }

  @Override
  public IJG_Planet planetById(String pID) {
    return mPlanets.stream().filter(planet -> planet.id().equals(pID)).findFirst().orElse(null);
  }

  @Override
  public IJG_Planet planetByName(String pName) {
    return mPlanets.stream().filter(planet -> planet.name().equals(pName)).findFirst().orElse(null);
  }

  @Override
  public List<IJG_Planet> planets() {
    return Collections.unmodifiableList(mPlanets );
  }

  @Override
  public void timeProgression(Duration pTimeStep) {

    for( IJG_Planet planet : planets() ) {
      planet.timeProgression(pTimeStep);
    }

    return;
  }
}
