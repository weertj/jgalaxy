package org.jgalaxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class startCommand {

  public static void main(String[] args) {

    Map<String, List<String>> parsedArgs = new HashMap<>(16);

    String currentKey = null;
    for (String arg : args) {
      if (arg.startsWith("-")) { // New flag detected
        currentKey = arg;
        parsedArgs.put(currentKey, new ArrayList<>());
      } else if (currentKey != null) { // Value for the last detected flag
        parsedArgs.get(currentKey).add(arg);
      }
    }
  }

  private static void printHelp() {
    System.out.println("Usage: java CommandTool [options]");
    System.out.println("Options:");
    System.out.println("  -h, --help            Show help.");
    System.out.println("  -v, --verbose         Enable verbose output.");
    System.out.println("  -f, --file <FILE>     Specify input file.");
    System.out.println("  -n, --number <NUMBER> Provide a number.");
    System.out.println("  -p, --planets <PLANETS> Comma-separated list of planets.");
  }

}
