package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;

import java.util.ArrayList;
import java.util.List;

public class JG_Fleet implements IJG_Fleet {

  static public IJG_Fleet of() {
    return new JG_Fleet();
  }

  private final IJG_Position    mPosition = JG_Position.of(0, 0);
  private final List<IJG_Unit>  mUnits = new ArrayList<>(8);

  private JG_Fleet() {
    return;
  }

  @Override
  public IJG_Position position() {
    return mPosition;
  }

  @Override
  public List<IJG_Unit> units() {
    return mUnits;
  }
}
