package org.jgalaxy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class GEN_Streams {

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
