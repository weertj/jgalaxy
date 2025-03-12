package org.jgalaxy.units;

import org.jgalaxy.tech.IJG_Tech;
import org.jgalaxy.tech.JG_Tech;
import org.junit.*;

public class JGL_UnitTest {

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
  public void testUnitTest() {
    IJG_Tech tech = JG_Tech.of(1,1,1,1);
    IJG_UnitDesign design = JG_UnitDesign.of(10,10,2,10,10, tech);
    IJG_Unit unit = JG_Unit.of( design );
    unit.setNumberOf(1);
    System.out.println(design.mass());
  }

}

