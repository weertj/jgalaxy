package org.jgalaxy;

import org.jgalaxy.utils.GEN_Math;

import java.util.Comparator;
import java.util.List;

public interface IJG_Position {

  static void sortByDistance(List<? extends IJG_Position> positions, IJG_Position reference) {
    positions.sort(Comparator.comparingDouble(p -> GEN_Math.distance(p, reference)));
    return;
  }

  double x();
  double y();

  /**
   * setX (round02)
   * @param x
   */
  void setX(double x);

  /**
   * setY (round02)
   * @param y
   */
  void setY(double y);

  /**
   * setPosition (as is)
   * @param x
   * @param y
   */
  void setPosition(double x, double y);

  default void copyOf( IJG_Position position ) {
    setX( position.x() );
    setY( position.y() );
    return;
  }

}
