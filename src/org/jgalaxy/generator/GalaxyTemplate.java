package org.jgalaxy.generator;


public class GalaxyTemplate implements IGalaxyTemplate {

  static public IGalaxyTemplate of() {
    return new GalaxyTemplate();
  }

  private final String mName;
  private final double mXStart;
  private final double mYStart;
  private final double mXEnd;
  private final double mYEnd;

  public GalaxyTemplate() {
    mName = "GenerateGame";
    mXStart = 0;
    mYStart = 0;
    mXEnd = 50;
    mYEnd = 50;
    return;
  }

  @Override
  public String name() {
    return mName;
  }

  @Override
  public double xStart() {
    return mXStart;
  }

  @Override
  public double yStart() {
    return mYStart;
  }

  @Override
  public double xEnd() {
    return mXEnd;
  }

  @Override
  public double yEnd() {
    return mYEnd;
  }
}
