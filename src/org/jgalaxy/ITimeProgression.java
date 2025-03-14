package org.jgalaxy;

import org.jgalaxy.engine.IJG_Game;

import java.time.Duration;

public interface ITimeProgression {

  void timeProgression(IJG_Game pGame, Duration pTimeStep);

}
