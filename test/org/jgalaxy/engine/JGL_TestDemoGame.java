package org.jgalaxy.engine;

import org.jgalaxy.generator.GalaxyGenerator;
import org.jgalaxy.generator.GalaxyTemplate;
import org.jgalaxy.generator.IGalaxyTemplate;
import org.jgalaxy.utils.XML_Utils;
import org.junit.*;

import java.io.File;

public class JGL_TestDemoGame {

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

//            <game name="TestGame4" timeProgressionDays="1460" turnHistory="2" turnIntervalSecs="10" runWhenAllOrdersAreIn="false" minDistBetweenPlanets="2" minDistBetweenHomePlanets="15">

      String xml = """
    <root>
        <game name="DemoGame" timeProgressionDays="1460" turnHistory="-1" turnIntervalSecs="-1" realtime="false" runWhenAllOrdersAreIn="false" minDistBetweenPlanets="4" minDistBetweenHomePlanets="30" maxDistBetweenHomePlanets="999">
          <map>
            <mapcol>
              <min x="0" y="0"/>
              <max x="100" y="100"/>
              <planet generate="80"/> 
            </mapcol>
          </map>
          <player generate="8">
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
  public void testGenerateGame2Test() throws Throwable {

//            <game name="TestGame4" timeProgressionDays="1460" turnHistory="2" turnIntervalSecs="10" runWhenAllOrdersAreIn="false" minDistBetweenPlanets="2" minDistBetweenHomePlanets="15">

    String xml = """
    <root>
        <game name="AIDemoBigGame" timeProgressionDays="1460" turnHistory="3" turnIntervalSecs="60" realtime="false" runWhenAllOrdersAreIn="false" minDistBetweenPlanets="4" minDistBetweenHomePlanets="30">
          <map>
            <mapcol>
              <min x="0" y="0"/>
              <max x="500" y="500"/>
              <planet generate="500"/> 
            </mapcol>
          </map>
          <player generate="30">
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

