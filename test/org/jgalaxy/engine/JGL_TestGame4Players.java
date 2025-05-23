package org.jgalaxy.engine;

import org.jgalaxy.generator.GalaxyGenerator;
import org.jgalaxy.generator.GalaxyTemplate;
import org.jgalaxy.generator.IGalaxyTemplate;
import org.jgalaxy.utils.XML_Utils;
import org.junit.*;

import java.io.File;

public class JGL_TestGame4Players {

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
  public void testGenerateGameTest() throws Throwable {

    String xml = """
    <root>
        <game name="TestGame4" gameType="Reloaded" timeProgressionDays="1460" realtime="false" turnNumber="1" turnHistory="-1" turnIntervalSecs="-1" runWhenAllOrdersAreIn="false" minDistBetweenPlanets="5" minDistBetweenHomePlanets="15" maxDistBetweenHomePlanets="30">
          <map>
            <mapcol>
              <min x="0" y="0"/>
              <max x="30" y="30"/>
              <planet generate="10"/> 
            </mapcol>
          </map>
          <player generate="4">
            <faction>
            </faction>
          </player>
        </game>
    </root>
      """;

    IGalaxyTemplate template = GalaxyTemplate.of(XML_Utils.rootNodeBy(xml));

    IJG_Game game = GalaxyGenerator.generate(template);
    game.roundUp();
    game.reconPhase(true);

    IJG_GameInfo gameInfo = JG_GameInfo.of(new File("workdir/games/" + template.name() ));
    gameInfo.init();

    File gamedir = new File("workdir/games/" + template.name() );

    game.storeObject(gamedir, null,null,"");

    return;
  }

  @Test
  public void testGenerateGameBigTest() throws Throwable {

    String xml = """
    <root>
        <game name="TestGame5" timeProgressionDays="1" realtime="true" turnNumber="1" turnHistory="-1" turnIntervalSecs="-1" runWhenAllOrdersAreIn="false" minDistBetweenPlanets="2" minDistBetweenHomePlanets="15" maxDistBetweenHomePlanets="30">
          <map>
            <mapcol>
              <min x="0" y="0"/>
              <max x="1500" y="1500"/>
              <planet generate="1000"/> 
            </mapcol>
          </map>
          <player generate="100">
            <faction>
            </faction>
          </player>
        </game>
    </root>
      """;

    IGalaxyTemplate template = GalaxyTemplate.of(XML_Utils.rootNodeBy(xml));

    IJG_Game game = GalaxyGenerator.generate(template);
    game.roundUp();
    game.reconPhase(true);

    IJG_GameInfo gameInfo = JG_GameInfo.of(new File("workdir/games/" + template.name() ));
    gameInfo.init();

    File gamedir = new File("workdir/games/" + template.name() );

    game.storeObject(gamedir, null,null,"");

    return;
  }


}

