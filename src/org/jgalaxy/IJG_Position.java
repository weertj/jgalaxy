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

  void setX(double x);
  void setY(double y);

  default void copyOf( IJG_Position position ) {
    setX( position.x() );
    setY( position.y() );
    return;
  }

}
