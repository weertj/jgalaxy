package org.jgalaxy.generator;

import org.w3c.dom.Node;

import java.util.List;

public interface IGalaxyTemplate {

  String name();

  double xStart();
  double xEnd();
  double yStart();
  double yEnd();

  Node gameNode();
  List<Node> planetGenerations();
  List<Node> playerGenerations();


}
