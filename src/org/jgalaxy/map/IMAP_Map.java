package org.jgalaxy.map;

import org.jgalaxy.IStorage;
import org.jgalaxy.ITimeProgression;
import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.planets.IJG_Planets;

import java.util.List;

public interface IMAP_Map extends ITimeProgression, IStorage {

  IJG_Planets planets();

}
