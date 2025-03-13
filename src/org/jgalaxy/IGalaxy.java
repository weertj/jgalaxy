package org.jgalaxy;

import org.jgalaxy.map.IMAP_Map;

public interface IGalaxy extends ITimeProgression, IStorage {

  IMAP_Map map();

}
