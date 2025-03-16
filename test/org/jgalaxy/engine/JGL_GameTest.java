package org.jgalaxy.engine;

import org.junit.*;

import java.io.File;
import java.time.Duration;

public class JGL_GameTest {

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testGameTest() throws Throwable {
    File gamedir = new File("workdir/games/test1");
    IJG_Game game = JG_Game.of( gamedir, 0 );
    IJG_Player player = game.getPlayerByName("Player 0");
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(gamedir, null, null,"");
    game = JG_Game.of( gamedir, game.turnNumber() );
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(gamedir, null, null,"");
    game = JG_Game.of( gamedir, game.turnNumber() );
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(gamedir, null, null,"");
    game = JG_Game.of( gamedir, game.turnNumber() );
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(gamedir, null, null,"");
    game = JG_Game.of( gamedir, game.turnNumber() );
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(gamedir, null, null,"");
    System.out.println(game.reportForPlayerAs(player,"plain"));
//    game.removeTurnNumber(gamedir,2);
//    game.removeTurnNumber(gamedir,3);
  }

//  @Test
//  public void testGame2Test() throws Throwable {
//    File gamedir = new File("workdir/games/test1");
//    IJG_Game game = JG_Game.of( gamedir, 0 );
//
//    for( int ix = 0; ix < 100; ix++) {
//      game.timeProgression(game, Duration.ofDays(1));
//      game.storeObject(gamedir, null, null);
//      game.removeTurnNumber(gamedir,ix-4);
//      game = JG_Game.of( gamedir, game.turnNumber() );
//    }
//  }

}

