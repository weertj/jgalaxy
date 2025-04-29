package org.jgalaxy.generator;


import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class GalaxyTemplate implements IGalaxyTemplate {

  static public IGalaxyTemplate of( Node pTemplateNode ) {
    return new GalaxyTemplate(pTemplateNode);
  }

  private final String mName;
  private final double mXStart;
  private final double mYStart;
  private final double mXEnd;
  private final double mYEnd;

  private final Node mTemplateNode;
  private final Node mGameNode;
  private final Node mMapNode;
  private final Node mMapColNode;

  private GalaxyTemplate(Node pTemplateNode) {
    mTemplateNode = pTemplateNode;
    mGameNode = XML_Utils.childNodeByPath(pTemplateNode,"game").get();
    mMapNode = XML_Utils.childNodeByPath(mGameNode,"map").get();
    mMapColNode = XML_Utils.childNodeByPath(mMapNode,"mapcol").get();

    mName = "GenerateGame";
    mXStart = 0;
    mYStart = 0;
    mXEnd = 200;
    mYEnd = 200;
    return;
  }

  @Override
  public long seed() {
    return 1234L;
  }

  @Override
  public String name() {
    return XML_Utils.attr(mGameNode,"name");
  }

  @Override
  public double xStart() {
    return Double.parseDouble(XML_Utils.attr(XML_Utils.childNodeByPath(mMapColNode,"min").get(),"x"));
  }

  @Override
  public double yStart() {
    return Double.parseDouble(XML_Utils.attr(XML_Utils.childNodeByPath(mMapColNode,"min").get(),"y"));
  }

  @Override
  public double xEnd() {
    return Double.parseDouble(XML_Utils.attr(XML_Utils.childNodeByPath(mMapColNode,"max").get(),"x"));
  }

  @Override
  public double yEnd() {
    return Double.parseDouble(XML_Utils.attr(XML_Utils.childNodeByPath(mMapColNode,"max").get(),"y"));
  }

  @Override
  public Node gameNode() {
    return mGameNode;
  }

  @Override
  public List<Node> planetGenerations() {
    List<Node> pnodes = new ArrayList<>();
    for( Node planet : XML_Utils.childElementsByName( mMapColNode, "planet" )) {
      String gen = XML_Utils.attr(planet,"generate");
      if (!gen.isBlank()) {
        pnodes.add(planet);
      }
    }
    return pnodes;
  }

  @Override
  public List<Node> playerGenerations() {
    List<Node> pnodes = new ArrayList<>();
    for( Node player : XML_Utils.childElementsByName( mGameNode, "player" )) {
      String gen = XML_Utils.attr(player,"generate");
      if (!gen.isBlank()) {
        pnodes.add(player);
      }
    }
    return pnodes;
  }

  @Override
  public boolean isRealtime() {
    return Boolean.parseBoolean(XML_Utils.attr( mGameNode, "realtime", "false" ));
  }

  @Override
  public double homePlanetMin() {
    return XML_Utils.attrd( mGameNode, "homePlanetMin", 1000.0 );
  }

  @Override
  public double homePlanetMax() {
    return XML_Utils.attrd( mGameNode, "homePlanetMax", 1000.0 );
  }

  @Override
  public double minDistBetweenPlanets() {
    return XML_Utils.attrd( mGameNode, "minDistBetweenPlanets", 5.0 );
  }

  @Override
  public double maxDistBetweenPlanets() {
    return XML_Utils.attrd( mGameNode, "maxDistBetweenPlanets", 100.0 );
  }

  @Override
  public double planetMinSize() {
    return XML_Utils.attrd( mGameNode, "planetMinSize", 100.0 );
  }

  @Override
  public double planetMaxSize() {
    return XML_Utils.attrd( mGameNode, "planetMaxSize", 1000.0 );
  }

  @Override
  public double homePlanetAttraction() {
    return XML_Utils.attrd( mGameNode, "homePlanetAttraction", 0.0 );
  }

  @Override
  public double minDistBetweenHomePlanets() {
    return XML_Utils.attrd( mGameNode, "minDistBetweenHomePlanets", 10.0 );
  }

  @Override
  public double maxDistBetweenHomePlanets() {
    return XML_Utils.attrd( mGameNode, "maxDistBetweenHomePlanets", 100.0 );
  }
}
