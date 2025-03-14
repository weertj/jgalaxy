package org.jgalaxy.planets;

import org.jgalaxy.JG_Position;
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
    IJG_Planet planet = JG_Planet.of(null,"p0", JG_Position.of(1,1));
    planet.setPopulation(10000);
    for( int i = 0; i < 10; i++ ) {
      System.out.println(planet);
      planet.timeProgression(null, Duration.ofDays(1));
    }
  }

}

