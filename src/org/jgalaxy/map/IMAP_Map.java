package org.jgalaxy.map;

import org.jgalaxy.IStorage;
import org.jgalaxy.ITimeProgression;
import org.jgalaxy.planets.IJG_Planets;

public interface IMAP_Map extends ITimeProgression, IStorage {

  IJG_Planets planets();

  double xStart();
  double yStart();
  double xEnd();
  double yEnd();

}
