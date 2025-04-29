package org.jgalaxy;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.image.Image;
import org.jgalaxy.engine.*;


public interface IGameContext {

  String gameName();
  void setGameName(String pGameName);
  String playerName();
  void setPlayerName(String pPlayerName);

  String userName();

  String getTurnNumber();
  void setTurnNumber(String number);
  void previousTurnNumber();
  void nextTurnNumber();
  StringProperty turnNumberProperty();

  void addFactionChangeListener( ChangeListener<Number> pFactionChangedListener );
  void removeFactionChangeListener( ChangeListener<Number> pFactionChangedListener );

  IJG_Games loadGames();
  IJG_GameInfo loadGameInfo();
  IJG_Faction loadFaction();
  void loadBanners();

  void sendCurrentOrders();
  void nextTurn() throws Exception;

  Image imageForFaction(IJG_Faction pFaction);

  default IJG_GameInfo currentGameInfo() {
    return currentGameInfoProperty().get();
  }

  ObjectProperty<IJG_Games> currentGamesProperty();
  ObjectProperty<IJG_GameInfo> currentGameInfoProperty();
  ObjectProperty<IJG_Game> currentGameProperty();
  ObjectProperty<IJG_Game> currentGameChangedProperty();
  ObjectProperty<IJG_Player> currentPlayerProperty();
  ObjectProperty<IJG_Player> currentPlayerChangedProperty();
  ObjectProperty<IJG_Faction> currentFactionProperty();
  ObjectProperty<IJG_Faction> currentFactionChangedProperty();

  IJG_Faction retrieveFactionByID( String pID );
  IJG_Faction resolveFaction( Object pFaction );

  default IJG_Game currentGame() {
    return currentGameProperty().get();
  }
  default IJG_Game currentGameChanged() {
    return currentGameChangedProperty().get();
  }
  default IJG_Faction currentFaction() {
    return currentFactionProperty().get();
  }
  default IJG_Faction currentFactionChanged() {
    return currentFactionChangedProperty().get();
  }
  default IJG_Player currentPlayer() {
    return currentPlayerProperty().get();
  }
  default IJG_Player currentPlayerChanged() {
    return currentPlayerChangedProperty().get();
  }


}
