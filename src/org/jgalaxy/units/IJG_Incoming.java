package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.IStorage;

public interface IJG_Incoming extends IStorage {

  IJG_Position from();
  IJG_Position current();
  IJG_Position to();
  Double mass();

}
