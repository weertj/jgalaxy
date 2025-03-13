package org.jgalaxy.engine;

import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.tech.JG_Tech;
import org.jgalaxy.units.IJG_Unit;
import org.jgalaxy.units.IJG_UnitDesign;
import org.jgalaxy.units.JG_Unit;
import org.jgalaxy.units.JG_UnitDesign;
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
    IJG_Player player = JG_Player.of(game,"player1", "Player 1");
    game.addPlayer(player);
    IJG_Faction faction = JG_Faction.of(game,"faction1", "Faction 1");
    game.addFaction(faction);
    player.addFaction(faction);
    System.out.println(game.reportForPlayerAs(player,"plain"));
    game.timeProgression(Duration.ofDays(1));
    game.storeObject(new File("workdir/games/test1"), null, null);
  }

}

