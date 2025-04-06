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
}
