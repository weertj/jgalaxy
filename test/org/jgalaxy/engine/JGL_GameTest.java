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
    IJG_Game game = JG_Game.of( new File("workdir/games/test1"), 0 );
    IJG_Player player = game.getPlayerByName("Player 1");
    System.out.println(game.reportForPlayerAs(player,"plain"));
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(new File("workdir/games/test1"), null, null);
    game = JG_Game.of( new File("workdir/games/test1"), game.turnNumber() );
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(new File("workdir/games/test1"), null, null);
    game = JG_Game.of( new File("workdir/games/test1"), game.turnNumber() );
    game.timeProgression(game, Duration.ofDays(4));
    game.storeObject(new File("workdir/games/test1"), null, null);
  }

}

