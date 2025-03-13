package org.jgalaxy.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GEN_Streams {


  static public void writeStringToFile( String pContents, File pF ) throws IOException {
    Files.write( pF.toPath(), pContents.getBytes(StandardCharsets.UTF_8) );
    return;
  }

  static public String readAsString(final InputStream pInput, final Charset pEncoding) throws IOException {
    if (pInput==null) {
      return "";
    }
    final StringBuilder buf;
    try (InputStreamReader isr = new InputStreamReader(pInput, pEncoding);
         BufferedReader br = new BufferedReader(isr)) {
      buf = new StringBuilder(128);
      String line;
      while ((line = br.readLine()) != null) {
        buf.append(line).append('\n');
      }
    } finally {
      pInput.close();
    }
    return buf.toString();
  }

}
