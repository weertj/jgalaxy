package org.jgalaxy;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.JG_Game;
import org.jgalaxy.utils.XML_Utils;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * startCommand
 */
public class startCommand {

//  public static void main(String[] args) {
//
//    try {
//      Map<String, String> parsedArgs = new HashMap<>(16);
//
//      String currentKey = null;
//      for (String arg : args) {
//        if (arg.startsWith("-")) {
//          String[] values = arg.split("=");
//          currentKey = values[0];
//          if (values.length>1) {
//            parsedArgs.put(currentKey, values[1] );
//          } else {
//            parsedArgs.put(currentKey, "");
//          }
//        }
//      }
//
//      String gameName = parsedArgs.get("-game");
//      int turnNumber = Integer.parseInt(parsedArgs.getOrDefault( "-turnNumber", "0" ));
//      File gamedir = new File(parsedArgs.getOrDefault("-d", "." ));
//      gamedir = new File(gamedir, gameName );
//      System.out.println("Directory: " + gamedir.getCanonicalPath());
//      IJG_Game game = JG_Game.of( gamedir, turnNumber );
//
//      if (parsedArgs.containsKey("-show")) {
//
//        if (parsedArgs.containsKey("-faction")) {
//          IJG_Faction faction = game.getFactionById(parsedArgs.getOrDefault("-faction", ""));
//          Document doc = XML_Utils.newXMLDocument();
//          Node root = doc.createElement("root");
//          doc.appendChild(root);
//          faction.storeObject(null, root, "", "");
//          System.out.println(XML_Utils.documentToString(doc));
//
//          JSONObject json = XML.toJSONObject(XML_Utils.documentToString(doc));
//          System.out.printf("JSON: %s\n", json.toString(2));
//
//        }
//
//      }
//
//    } catch ( Throwable e ) {
//      e.printStackTrace();
//    }
//    return;
//
//  }
//
//  private static void printHelp() {
//    System.out.println("Usage: java CommandTool [options]");
//    System.out.println("Options:");
//    System.out.println("  -h, --help            Show help.");
//    System.out.println("  -v, --verbose         Enable verbose output.");
//    System.out.println("  -f, --file <FILE>     Specify input file.");
//    System.out.println("  -n, --number <NUMBER> Provide a number.");
//    System.out.println("  -p, --planets <PLANETS> Comma-separated list of planets.");
//  }



  public static void main(String[] args) {
    if (args.length == 0) {
      printHelp();
      return;
    }

    Map<String, String> parsedArgs = new HashMap<>();
    List<String> positionalArgs = new ArrayList<>();

    // Parse arguments
    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.startsWith("--")) {
        String[] split = arg.substring(2).split("=", 2);
        parsedArgs.put(split[0], split.length > 1 ? split[1] : "");
      } else if (arg.startsWith("-")) {
        if (arg.length() > 2 && arg.contains("=")) {
          String[] split = arg.substring(1).split("=", 2);
          parsedArgs.put(split[0], split[1]);
        } else if (arg.length() > 2) {
          for (int j = 1; j < arg.length(); j++) {
            parsedArgs.put(String.valueOf(arg.charAt(j)), "");
          }
        } else {
          parsedArgs.put(arg.substring(1), "");
        }
      } else {
        positionalArgs.add(arg);
      }
    }

    try {
      if (parsedArgs.containsKey("h") || parsedArgs.containsKey("help")) {
        printHelp();
        return;
      }

      String gameName = parsedArgs.getOrDefault("game", "");
      int turnNumber = Integer.parseInt(parsedArgs.getOrDefault("turnNumber", parsedArgs.getOrDefault("t","0")));
      File mainDir = new File(parsedArgs.getOrDefault("d", "."));
      File gameDir = new File(mainDir, gameName);
      System.out.println("Directory: " + gameDir.getCanonicalPath());

      IJG_Game game = JG_Game.of(gameDir, null,turnNumber);
      IJG_Faction faction = null;
      if (parsedArgs.containsKey("faction") || parsedArgs.containsKey("f")) {
        faction = game.getFactionById(parsedArgs.getOrDefault("faction", parsedArgs.getOrDefault("f", "")));
      }

      String result = "";

      // ********************************************************************
      // **** Show
      if (positionalArgs.contains("show")) {
        if (faction==null) {
          System.err.println("Please specify a faction with --faction or -f.");
        } else {
          Document doc = XML_Utils.newXMLDocument();
          Node root = doc.createElement("root");
          doc.appendChild(root);
          faction.storeObject(null, root, "", "");

          if (parsedArgs.containsKey("json")) {
            result = XML.toJSONObject(XML_Utils.documentToString(doc)).toString(2);
          } else {
            result = XML_Utils.documentToString(doc);
          }
        }
      }

      // ********************************************************************
      // **** Delete
      if (positionalArgs.contains("delete")) {
        game.removeTurnNumber( gameDir, turnNumber );
        result = "Turnnumber: " + turnNumber + " deleted";
      }

      // ********************************************************************
      // **** Run turnnumber
      if (positionalArgs.contains("run")) {
        game.timeProgression(game, Duration.ofHours(24));
        game.storeObject(gameDir, null, null,"");
        result = "Turnnumber: " + turnNumber + " runned";
      }

      System.out.println(result);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void printHelp() {
    System.out.println("Usage: java -cp your.jar org.jgalaxy.EnhancedCommandTool [options] <command>");
    System.out.println("Commands:");
    System.out.println("  show                     Display faction data.");
    System.out.println("      Options:");
    System.out.println("  -h, --help               Show help.");
    System.out.println("  -g, --game <NAME>        Specify the game name.");
    System.out.println("  -t, --turnNumber <NUM>    Specify the turn number.");
    System.out.println("  -d <DIR>                 Specify the game directory.");
    System.out.println("  -f, --faction <NAME>      Specify the faction ID.");
    System.out.println("  --json                   Output in JSON format.");
  }

}
