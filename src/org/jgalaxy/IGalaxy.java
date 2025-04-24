package org.jgalaxy;

import org.jgalaxy.map.IMAP_Map;

public interface IGalaxy extends ITimeProgression, IStorage {

  IGalaxy copyOf();

  IMAP_Map map();

}
