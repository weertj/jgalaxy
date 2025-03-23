package org.jgalaxy.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jgalaxy.IStorage;
import org.jgalaxy.engine.*;
import org.jgalaxy.orders.JG_Orders;
import org.jgalaxy.utils.XML_Utils;
import org.json.XML;
import org.w3c.dom.Document;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class SimpleServer {

  private int mPort = 8080;

  static private class GamesHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
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
            storage = game;
            if (path.length>5) {
              IJG_Player player = game.getPlayerByID(path[5]);
              storage = player;
              if (path.length>6) {
                IJG_Faction faction = player.getFactionByID(path[6]);
                storage = faction;
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
      } else {
        exchange.sendResponseHeaders(405, -1); // Method Not Allowed
      }
    }

  }

  private SimpleServer() throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(mPort), 0);
    server.setExecutor(null); // Uses the default executor
    server.createContext("/jgalaxy/games", new GamesHandler());
    server.start();
    return;
  }

  public static void main(String[] args) throws IOException {
    SimpleServer server = new SimpleServer();
  }

}
