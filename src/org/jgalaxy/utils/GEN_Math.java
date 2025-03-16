package org.jgalaxy.utils;

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

}
