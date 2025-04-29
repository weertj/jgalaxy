package org.jgalaxy;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import org.jgalaxy.engine.*;
import org.jgalaxy.orders.IJG_Orders;
import org.jgalaxy.orders.JG_Orders;
import org.jgalaxy.server.SimpleClient;
import org.jgalaxy.utils.GEN_Streams;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class GameContext implements IGameContext {

  static public IGameContext of(String pDirectory,String pURL, String pGameName, String pPlayerName, String pUsername, String pPassword) {
    return new GameContext(pDirectory,pURL, pGameName, pPlayerName, pUsername, pPassword);
  }

  private String mDirectory;
  private String mURL;
  private String mGameName;
  private String mPlayerName;
  private String mUserName;
  private String mPassword;
//  private String mTurnNumber;

  private final StringProperty                mCurrentTurnNumber = new SimpleStringProperty();
  private final ObjectProperty<IJG_Games>     mCurrentGames = new SimpleObjectProperty<>();
  private final ObjectProperty<IJG_GameInfo>  mCurrentGameInfo = new SimpleObjectProperty<>();
  private final ObjectProperty<IJG_Game>      mCurrentGame = new SimpleObjectProperty<>();
  private final ObjectProperty<IJG_Game>      mCurrentGameChanged = new SimpleObjectProperty<>();
  private final ObjectProperty<IJG_Player>    mCurrentPlayer = new SimpleObjectProperty<>();
  private final ObjectProperty<IJG_Player>    mCurrentPlayerChanged = new SimpleObjectProperty<>();
  private final ObjectProperty<IJG_Faction>   mCurrentFaction = new SimpleObjectProperty<>();
  private final ObjectProperty<IJG_Faction>   mCurrentFactionChanged = new SimpleObjectProperty<>();

  public final Map<String, Image> mBanners = new HashMap<>();

  private final List<ChangeListener<Number>> mFactionChangedListeners = new ArrayList<>(8);

  private final ChangeListener<Number> mFactionChanged = (observable, oldValue, newValue) -> {
    for( var fl : mFactionChangedListeners ) {
      fl.changed(observable, oldValue, newValue);
    }
    return;
  };


  private GameContext(String pDirectory,String pURL, String pGameName, String pPlayerName, String pUsername, String pPassword) {
    mDirectory = pDirectory;
    mURL = pURL;
    mGameName = pGameName;
    mPlayerName = pPlayerName;
    mUserName = pUsername;
    mPassword = pPassword;
    return;
  }

  @Override
  public String gameName() {
    return mGameName;
  }

  @Override
  public void setGameName(String pGameName) {
    mGameName = pGameName;
    return;
  }

  @Override
  public String playerName() {
    return mPlayerName;
  }

  @Override
  public String userName() {
    return mUserName;
  }

  @Override
  public void setPlayerName(String pPlayerName) {
    mPlayerName = pPlayerName;
    return;
  }

  @Override
  public void setTurnNumber(String number) {
    mCurrentTurnNumber.setValue(number);
    return;
  }

  @Override
  public void previousTurnNumber() {
    Long t = Long.parseLong(mCurrentTurnNumber.get());
    setTurnNumber("" + (t.longValue()-1));
    return;
  }

  @Override
  public void nextTurnNumber() {
    Long t = Long.parseLong(mCurrentTurnNumber.get());
    setTurnNumber("" + (t.longValue()+1));
    return;
  }

  @Override
  public StringProperty turnNumberProperty() {
    return mCurrentTurnNumber;
  }

  @Override
  public String getTurnNumber() {
    return mCurrentTurnNumber.get();
  }

  @Override
  public ObjectProperty<IJG_Games> currentGamesProperty() {
    return mCurrentGames;
  }

  @Override
  public ObjectProperty<IJG_GameInfo> currentGameInfoProperty() {
    return mCurrentGameInfo;
  }

  @Override
  public ObjectProperty<IJG_Game> currentGameProperty() {
    return mCurrentGame;
  }

  @Override
  public ObjectProperty<IJG_Game> currentGameChangedProperty() {
    return mCurrentGameChanged;
  }

  @Override
  public ObjectProperty<IJG_Player> currentPlayerProperty() {
    return mCurrentPlayer;
  }

  @Override
  public ObjectProperty<IJG_Player> currentPlayerChangedProperty() {
    return mCurrentPlayerChanged;
  }

  @Override
  public ObjectProperty<IJG_Faction> currentFactionProperty() {
    return mCurrentFaction;
  }

  @Override
  public ObjectProperty<IJG_Faction> currentFactionChangedProperty() {
    return mCurrentFactionChanged;
  }


  @Override
  public IJG_Faction retrieveFactionByID( String pID ) {
    if (currentFactionChanged()!=null && Objects.equals(currentFactionChanged().id(), pID)) {
      return currentFactionChanged();
    }
    if (currentPlayerChanged()!=null) {
      return currentPlayerChanged().getFactionByID(pID);
    }
    return null;
  }

  @Override
  public IJG_Faction resolveFaction( Object pFaction ) {
    if (pFaction instanceof IJG_Faction f) {
      return currentFactionChanged().resolveFactionById(f.id());
    }
    if (pFaction instanceof String fid) {
      return currentFactionChanged().resolveFactionById(fid);
    }
    return null;
  }

  @Override
  public IJG_Games loadGames() {
    if (mDirectory==null) {
      String url = mURL;
      HttpRequest request = HttpRequest.newBuilder(URI.create(url + "?alt=xml"))
        .GET()
        .build();
      Node root;
      try {
        HttpResponse response = SimpleClient.createClient(mUserName, mPassword).send(request, HttpResponse.BodyHandlers.ofString());
        String result = response.body().toString();
        root = XML_Utils.rootNodeBy(result);
        mCurrentGames.set(JG_Games.of(XML_Utils.childNodeByPath(root, "games").get()));
      } catch (Throwable e) {
        e.printStackTrace();
      }

    } else {
      try {
        mCurrentGames.set(JG_Games.of(new File(mDirectory)));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return mCurrentGames.get();
  }

  @Override
  public IJG_GameInfo loadGameInfo() {
    if (mDirectory==null) {
      String url = mURL;
      url += "/" + mGameName;
      HttpRequest request = HttpRequest.newBuilder(URI.create(url + "?alt=xml"))
        .GET()
        .build();
      Node root;
      try {
        HttpResponse response = SimpleClient.createClient(mUserName, mPassword).send(request, HttpResponse.BodyHandlers.ofString());
        String result = response.body().toString();
        root = XML_Utils.rootNodeBy(result);
        mCurrentGameInfo.set(JG_GameInfo.of(XML_Utils.childNodeByPath(root, "game").get()));
      } catch (Throwable e) {
        e.printStackTrace();
      }
    } else {
      try {
        mCurrentGameInfo.set(JG_GameInfo.of(new File(mDirectory, mGameName)));
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }
    return mCurrentGameInfo.get();
  }

  @Override
  public void addFactionChangeListener(ChangeListener<Number> pFactionChangedListener) {
    if (!mFactionChangedListeners.contains(pFactionChangedListener)) {
      mFactionChangedListeners.add(pFactionChangedListener);
    }
    return;
  }

  @Override
  public void removeFactionChangeListener(ChangeListener<Number> pFactionChangedListener) {
    mFactionChangedListeners.remove(pFactionChangedListener);
    return;
  }

  @Override
  public IJG_Faction loadFaction() {

    // **** File based
    if (mDirectory!=null) {
      try {
        mCurrentGame.set(JG_Game.of(new File(mDirectory,mGameName),null,Long.parseLong(mCurrentTurnNumber.get())));
        mCurrentGameChanged.set(JG_Game.of(new File(mDirectory,mGameName),null,Long.parseLong(mCurrentTurnNumber.get())));
        IJG_Player player = mCurrentGame.get().getPlayerByID(mPlayerName);
        mCurrentPlayer.set( player );
        IJG_Player playerchanged = mCurrentGameChanged.get().getPlayerByID(mPlayerName);
        mCurrentPlayerChanged.set( playerchanged );
        IJG_Faction faction = mCurrentGame.get().getFactionById(player.factions().getFirst().id() );
        mCurrentFaction.set( faction );
        IJG_Faction factionchanged = mCurrentGameChanged.get().getFactionById(player.factions().getFirst().id() );
        mCurrentFactionChanged.set( factionchanged );
        factionchanged.changeCounterProperty().addListener(mFactionChanged);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      return mCurrentFactionChanged.get();
    }



    String url = mURL;
    url += "/" + mGameName;
    if (mCurrentGameChanged.get()!=null && mCurrentGameChanged.get().isRealTime()) {
      url += "/current";
    } else {
      url += "/" + getTurnNumber();
    }
    HttpRequest request = HttpRequest.newBuilder(URI.create(url + "?alt=xml"))
      .GET()
      .build();
    Node root = null;
    IJG_Game game = null;
    IJG_Game gamechanged = null;
    try {
      HttpResponse response = SimpleClient.createClient(mUserName, mPassword).send(request, HttpResponse.BodyHandlers.ofString());
      String result = response.body().toString();
      root = XML_Utils.rootNodeBy(result);
      game = JG_Game.of(null, root, Long.parseLong(getTurnNumber()));
      mCurrentGame.set(game);
      gamechanged = JG_Game.of(null, root, Long.parseLong(getTurnNumber()));
      mCurrentGameChanged.set(gamechanged);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    IJG_Player player = game.getPlayerByID(mPlayerName);
    mCurrentPlayer.set( player );
    IJG_Player playerchanged = gamechanged.getPlayerByID(mPlayerName);
    mCurrentPlayerChanged.set( playerchanged );

    url += "/" + player.id() + "/" + player.factions().getFirst().id();

    request = HttpRequest.newBuilder(URI.create(url + "?alt=xml"))
      .GET()
      .build();
    root = null;
    try {
      HttpResponse response = SimpleClient.createClient(mUserName, mPassword).send(request, HttpResponse.BodyHandlers.ofString() );
      String result = response.body().toString();
      root = XML_Utils.rootNodeBy(result);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
    IJG_Faction faction = JG_Faction.of(game,XML_Utils.childNodeByPath(root,"faction").orElse(null));
    if (mCurrentFaction.get() != null) {
      mCurrentFaction.get().close();
    }
    mCurrentFaction.set( faction );
    IJG_Faction factionchanged = JG_Faction.of(gamechanged,XML_Utils.childNodeByPath(root,"faction").orElse(null));
    if (mCurrentFactionChanged.get() != null) {
//      mFactionChangedListeners.stream().forEach( mCurrentFactionChanged.get().changeCounterProperty()::removeListener );
      mCurrentFactionChanged.get().changeCounterProperty().removeListener(mFactionChanged);
      mCurrentFactionChanged.get().close();
    }
    mCurrentFactionChanged.set( factionchanged );

    factionchanged.changeCounterProperty().addListener(mFactionChanged);
//    mFactionChangedListeners.stream().forEach( mCurrentFactionChanged.get().changeCounterProperty()::addListener );

    return mCurrentFactionChanged.get();

  }



//    IJG_Faction factionchanged = JG_Faction.of(gamechanged,XML_Utils.childNodeByPath(root,"faction").orElse(null));
//    if (Global.CURRENTFACTION_CHANGED.get() != null) {
//      Global.CURRENTFACTION_CHANGED.get().changeCounterProperty().removeListener(mFactionChanged);
//      Global.CURRENTFACTION_CHANGED.get().close();
//    }
//    Global.CURRENTFACTION_CHANGED.set( factionchanged );


  @Override
  public void loadBanners() {
    mBanners.clear();
    for( String faction : mCurrentGameInfo.get().factions()) {
      Image banner = loadBanner( faction);
      if (banner!=null) {
        mBanners.put(faction, banner);
      }
    }
    return;
  }

  @Override
  public Image imageForFaction(IJG_Faction pFaction) {
    return mBanners.get(pFaction.id());
  }

  private Image loadBanner(String pFaction) {
    String url = mURL;
    url += "/" + mGameName + "/banners/" + pFaction + ".png";
    HttpRequest request = HttpRequest.newBuilder(URI.create(url))
      .GET()
      .build();
    try {
      HttpResponse response = SimpleClient.createClient(mUserName, mPassword).send(request, HttpResponse.BodyHandlers.ofByteArray() );
      if (response.statusCode()==200) {
        var bais = new ByteArrayInputStream((byte[]) response.body());
        return new Image(bais);
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return null;
  }


  @Override
  public void sendCurrentOrders() {

    if (mDirectory!=null) {
      IJG_Orders orders = JG_Orders.generateOf( Long.parseLong(getTurnNumber()), mCurrentFaction.get(), mCurrentFactionChanged.get());
      mCurrentFactionChanged.get().setOrders(orders);
      return;
    }


    IJG_Orders orders = JG_Orders.generateOf( Long.parseLong(getTurnNumber()), mCurrentFaction.get(), mCurrentFactionChanged.get());
    Document doc = XML_Utils.newXMLDocument();
    Node root = doc.createElement("root");
    doc.appendChild(root);
    orders.storeObject(null, root, "", "");
    try {
      String result = XML_Utils.documentToString(doc);
      System.out.println(result);

      String url =
          mURL + "/" +
          mGameName + "/" +
          getTurnNumber() + "/" +
          mPlayerName + "/" +
          mCurrentFaction.get().id() + "/" +
          "orders?alt=xml";
      HttpRequest request = HttpRequest.newBuilder(URI.create(url))
        .PUT(HttpRequest.BodyPublishers.ofString(result))
        .build();
      HttpResponse response = SimpleClient.createClient(mUserName, mPassword).send(request, HttpResponse.BodyHandlers.ofString());
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return;
  }

  @Override
  public void nextTurn() throws Exception {

    if (mDirectory!=null) {
      IJG_Game game = mCurrentGameChanged.get();
      game.setGameInfo(mCurrentGameInfo.get());
      game.timeProgression( game, Duration.ofDays((long)game.timeProgressionDays()));
      game.calcNextRun();
      return;
    }

    String url = mURL + "/" + mGameName + "/" + getTurnNumber() + "?nextTurn&alt=xml";
    HttpRequest request = HttpRequest.newBuilder(URI.create(url))
      .PUT(HttpRequest.BodyPublishers.ofString(""))
      .build();
    HttpResponse response = SimpleClient.createClient(mUserName, mPassword).send(request, HttpResponse.BodyHandlers.ofString() );
    return;
  }


}
