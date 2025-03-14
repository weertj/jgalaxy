package org.jgalaxy;

public interface IJG_Position {

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
