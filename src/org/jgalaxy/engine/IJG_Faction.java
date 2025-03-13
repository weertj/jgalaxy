package org.jgalaxy.engine;

import org.jgalaxy.IEntity;
import org.jgalaxy.IStorage;
import org.jgalaxy.units.IJG_UnitDesign;

import java.util.List;

public interface IJG_Faction extends IEntity, IStorage {

  void addUnitDesign( IJG_UnitDesign pDesign );
  List<IJG_UnitDesign> unitDesigns();

}
