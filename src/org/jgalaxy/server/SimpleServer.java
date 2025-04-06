package org.jgalaxy.server;

import com.sun.net.httpserver.*;
import org.jgalaxy.IStorage;
import org.jgalaxy.engine.*;
import org.jgalaxy.orders.JG_Orders;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleServer {

  private int mPort = 8765;

  static private class GamesHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

      String username = exchange.getPrincipal().getUsername();

      if ("GET".equals(exchange.getRequestMethod())) {
        URI uri = exchange.getRequestURI();
        System.out.println(uri.toString());
        String query = uri.getQuery()==null?"":uri.getQuery();
        String[] path = uri.getPath().split("/");
        String response = "";
        try {
          File gamedir = new File("workdir/games/" + path[3]);
          IJG_GameInfo gameInfo = JG_GameInfo.of(gamedir);
          IStorage storage = gameInfo;
          if (path.length>4) {
            IJG_Game game = JG_Game.of(gamedir,null, Integer.parseInt(path[4]));
            game.prepareGameAsUser(username);
            storage = game;
            if (path.length>5) {
              IJG_Player player = game.getPlayerByID(path[5]);
              if (!Objects.equals(player.getUsername(),username)) {
                exchange.sendResponseHeaders(401, -1);
                return;
              }
              storage = player;
              if (path.length>6) {
                IJG_Faction faction = player.getFactionByID(path[6]);
                storage = faction;
                if (path.length>7) {
                  if (path[7].equals("orders")) {
                    storage = faction.orders();
                  }
                }
              }
            }
          }
          Document doc = XML_Utils.newXMLDocument();
          var root = doc.createElement("root");
          doc.appendChild(root);
          storage.storeObject(null, root, "", "");
          response = XML_Utils.documentToString(doc);

          String contentType = "application/xml";
          if (query.contains("alt=json")) {
            contentType = "application/json";
            response = XML.toJSONObject(response).toString(2);
          }
          exchange.getResponseHeaders().add("Content-Type", contentType);
        } catch (Throwable e ) {
          e.printStackTrace();
        }
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
          os.write(response.getBytes(StandardCharsets.UTF_8));
        }
//      } else {
//        exchange.sendResponseHeaders(405, -1); // Method Not Allowed
      }

      if ("PUT".equals(exchange.getRequestMethod())) {
        URI uri = exchange.getRequestURI();
        System.out.println(uri.toString());
        String query = uri.getQuery()==null?"":uri.getQuery();
        String[] path = uri.getPath().split("/");
        String response = "";
        try {
          File gamedir = new File("workdir/games/" + path[3]);
          IJG_GameInfo gameInfo = JG_GameInfo.of(gamedir);
          IStorage storage = gameInfo;
          if (path.length>4) {
            IJG_Game game = JG_Game.of(gamedir,null, Integer.parseInt(path[4]));
            if (path.length>5) {
              IJG_Player player = game.getPlayerByID(path[5]);
              if (path.length>6) {
                IJG_Faction faction = player.getFactionByID(path[6]);
                if (path.length>7) {
                  if (path[7].equals("orders")) {
                    String orders = GEN_Streams.readAsString(exchange.getRequestBody(), Charset.defaultCharset());
                    Node root = XML_Utils.rootNodeBy(orders);
                    faction.setOrders(JG_Orders.of(game.turnNumber(),XML_Utils.childNodeByPath(root,"orders").get()));
                    File factionDir = JG_Faction.getFactionDirectory( new File("workdir"), gameInfo, faction );
                    GEN_Streams.writeStringToFile( orders, new File(factionDir,"orders_" + game.turnNumber() + ".xml" ));
                  }
                }
              }
            } else {
              String[] qelems = query.split("&");
              for (String qelem : qelems) {
                String[] qval = qelem.split("=");
                if ("nextTurn".equals(qelem)) {
                  nextTurn( gamedir, gameInfo,game );
//                  game.timeProgression( game, Duration.ofDays((long)game.timeProgressionDays()));
//                  game.calcNextRun();
//                  game.storeObject(gamedir, null, null,"");
//                  if (game.nextRun()!=null) {
//                    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//                    ZonedDateTime zdt = ZonedDateTime.parse(game.nextRun(), formatter);
//                    TimerTask task = new TimerTask() {
//                      @Override
//                      public void run() {
//
//                      }
//                    };
//                    Timer timer = new Timer();
//                    timer.schedule(task, Date.from(zdt.toInstant()));
//                  }
                }
              }
            }
          }
        } catch (Throwable e ) {
          e.printStackTrace();
        }
        exchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
          os.write(response.getBytes(StandardCharsets.UTF_8));
        }
      } else {
        exchange.sendResponseHeaders(405, -1); // Method Not Allowed
      }


    }

  }

  /**
   * nextTurn
   * @param pDir
   * @param pGameInfo
   * @param pGame
   */
  static private void nextTurn( File pDir, IJG_GameInfo pGameInfo, IJG_Game pGame ) {
    pGame.setGameInfo(pGameInfo);
    pGame.timeProgression( pGame, Duration.ofDays((long)pGame.timeProgressionDays()));
    pGame.calcNextRun();
    pGame.storeObject(pDir, null, null,"");
    pGame.aiPhase(); // **** Run AI for next orders
    if (pGame.nextRun()==null) {
      if (pGame.runWhenAllOrdersAreIn()) {
        // **** Do all orders in run check
        long turnNumber = pGame.turnNumber();
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
          @Override
          public void run() {
            try {
              IJG_Game game = JG_Game.of(pDir, null, turnNumber);
              game.setGameInfo(pGameInfo);
              int ordersIn = game.factions().stream().filter(f -> f.orders()!=null).toList().size();
              if (ordersIn==game.factions().size()) {
                timer.cancel();
                nextTurn(pDir, pGameInfo,game);
              }
            } catch (Throwable t) {
              t.printStackTrace();
            }
          }
        };
        timer.schedule(task, 5000L, 5000L);
        if (pGame.turnNumber()>1) {
          pGame.removeTurnNumber(pDir, pGame.turnNumber() - 1);
        }
      }
    } else {
      DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
      ZonedDateTime zdt = ZonedDateTime.parse(pGame.nextRun(), formatter);
      long turnNumber = pGame.turnNumber();
      TimerTask task = new TimerTask() {
        @Override
        public void run() {
          try {
            IJG_Game game = JG_Game.of(pDir, null, turnNumber);
            game.setGameInfo(pGameInfo);
            nextTurn(pDir, pGameInfo,game);
          } catch (Throwable e) {
            e.printStackTrace();
          }
        }
      };
      zdt = zdt.plusSeconds(pGame.turnIntervalSecs());
      Timer timer = new Timer();
      timer.schedule(task, Date.from(zdt.toInstant()));
      if (pGame.turnNumber()>1) {
        pGame.removeTurnNumber(pDir, pGame.turnNumber() - 1);
      }
    }
    return;
  }

  private SimpleServer() throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(mPort), 0);
    server.setExecutor(null); // Uses the default executor
    HttpContext context = server.createContext("/jgalaxy/games", new GamesHandler());
    context.setAuthenticator(new BasicAuthenticator("jgalaxy") {
      @Override
      public boolean checkCredentials(String username, String password) {
        if ("weert".equals(username) && "weert".equals(password)) {
          return true;
        }
        return false;
      }
    });
    server.start();
    return;
  }

  public static void main(String[] args) throws IOException {
    SimpleServer server = new SimpleServer();
  }

}
