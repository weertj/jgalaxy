package org.jgalaxy.planets;

import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.tech.JG_Tech;
import org.jgalaxy.units.IJG_Unit;
import org.jgalaxy.units.IJG_UnitDesign;
import org.jgalaxy.units.JG_Unit;
import org.jgalaxy.units.JG_UnitDesign;
import org.junit.*;

import java.time.Duration;

public class JGL_PlanetTest {

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
  public void testPlanetTest() {
    IJG_Planet planet = JG_Planet.of();
    planet.setPopulation(10000);
    for( int i = 0; i < 10; i++ ) {
      System.out.println(planet);
      planet.timeProgression(Duration.ofDays(1));
    }
  }

}

