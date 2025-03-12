package org.jgalaxy;

import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.engine.JG_Game;

import java.io.File;

public class startGame {

  public static void main(String[] args) {

    try {
      IJG_Game game = JG_Game.of(new File("games/test1/game.xml"));

    } catch (Throwable e) {
      e.printStackTrace();
    }
    return;
  }

}
