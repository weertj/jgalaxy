package org.jgalaxy.utils;

import org.jgalaxy.IJG_Position;

import java.util.Arrays;
import java.util.Random;
import java.util.SplittableRandom;

public class GEN_Math {

  static private GEN_Math MATH = new GEN_Math(1234);

  static public GEN_Math math() {
    return MATH;
  }

  private final Random            mRandom;
  private       SplittableRandom  mRandomS;

  private GEN_Math( final long pSeed ) {
    mRandom  = new Random(pSeed);
    mRandomS = new SplittableRandom(pSeed);
    return;
  }

  private SplittableRandom randoms() {
    return mRandomS;
  }

  public double nextRandom() {
    return mRandom.nextDouble();
  }

  public int randomRange( final int pMin, final int pMax ) {
    return randoms().nextInt( pMax - pMin + 1) + pMin;
  }


  static public double distance( final double pX1, final double pY1, final double pX2, final double pY2 ) {
    double x = pX1-pX2;
    double y = pY1-pY2;
    x = (x<=0.0) ? 0.0 - x : x;
    y = (y<=0.0) ? 0.0 - y : y;
    return Math.sqrt( x*x + y*y );
  }

  static public double distance( IJG_Position pP1, IJG_Position pP2 ) {
    return distance( pP1.x(), pP1.y(), pP2.x(), pP2.y() );
  }

  static public double round01( double pN ) {
    return Math.round(pN * 10.0) / 10.0;
  }

  static public double round02( double pN ) {
    return Math.round(pN * 100.0) / 100.0;
  }


}
