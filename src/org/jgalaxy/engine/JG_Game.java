package org.jgalaxy.engine;

import org.jgalaxy.Galaxy;
import org.jgalaxy.IGalaxy;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.map.MAP_Map;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;

public class JG_Game implements IJG_Game {

  static public IJG_Game of(File pFile ) throws IOException, ParserConfigurationException, SAXException {

    IGalaxy galaxy;
    try(FileInputStream fis = new FileInputStream(pFile)) {
      String xml = GEN_Streams.readAsString(fis, Charset.defaultCharset());
      Node root = XML_Utils.rootNodeBy(xml);
      Node gameNode = XML_Utils.childNodeByPath(root,"game").get();
      IMAP_Map map = MAP_Map.of( XML_Utils.childNodeByPath(gameNode, "map" ).get());
      galaxy = Galaxy.of(map);
    }

    return new JG_Game(galaxy);
  }

  private final IGalaxy mGalaxy;

  private JG_Game( IGalaxy pGalaxy ) {
    mGalaxy = pGalaxy;
    return;
  }

  @Override
  public void timeProgression(Duration pTimeStep) {
    mGalaxy.timeProgression(pTimeStep);
    return;
  }
}
