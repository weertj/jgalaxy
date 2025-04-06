package org.jgalaxy.engine;

import org.jgalaxy.generator.GalaxyGenerator;
import org.jgalaxy.generator.GalaxyTemplate;
import org.jgalaxy.generator.IGalaxyTemplate;
import org.jgalaxy.utils.XML_Utils;
import org.junit.*;

import java.io.File;
import java.time.Duration;

public class JGL_GenerateGameTest {

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
        <game name="GenerateGame" timeProgressionDays="365" runWhenAllOrdersAreIn="true">
          <map>
            <mapcol>
              <min x="0" y="0"/>
              <max x="10" y="10"/>
              <planet generate="2"/> 
            </mapcol>
          </map>
          <player generate="2">
            <faction>
            </faction>
          </player>
        </game>
    </root>
      """;

    IGalaxyTemplate template = GalaxyTemplate.of(XML_Utils.rootNodeBy(xml));

    IJG_Game game = GalaxyGenerator.generate(template);

    IJG_GameInfo gameInfo = JG_GameInfo.of(new File("workdir/games/" + template.name() ));
    gameInfo.init();

    File gamedir = new File("workdir/games/" + template.name() );

    game.storeObject(gamedir, null,null,"");

    return;
  }


}

