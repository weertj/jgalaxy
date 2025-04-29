package org.jgalaxy.generator;

import org.w3c.dom.Node;

import java.util.List;

public interface IGalaxyTemplate {

  long seed();

  String name();

  double xStart();
  double xEnd();
  double yStart();
  double yEnd();

  Node gameNode();
  List<Node> planetGenerations();
  List<Node> playerGenerations();

  boolean isRealtime();

  double homePlanetMin();
  double homePlanetMax();

  double planetMinSize();
  double planetMaxSize();

  double minDistBetweenPlanets();
  double maxDistBetweenPlanets();

  double homePlanetAttraction();

  double minDistBetweenHomePlanets();
  double maxDistBetweenHomePlanets();

}
