package org.jgalaxy.orders;

import org.jgalaxy.planets.IJG_Planet;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_UnitDesign;

public class SJG_LoadOrder {

  static public double loadOrder( IJG_Group pGroup, IJG_UnitDesign pUnitDesign, String pLoad, IJG_Planet pPlanet, double pAmountToLoad ) {
    double loaded = 0.0;
    double load = pAmountToLoad;
    double cc = pUnitDesign.canCarry(pGroup.tech());
    double already = pGroup.load();
    cc -= already;
    String currentCarry = pGroup.loadType();
    if (currentCarry.isBlank() || currentCarry.equals(pLoad)) {
      if (cc > 0.0) {
        if ("COL".equals(pLoad)) {
          double available = pPlanet.cols();
          load = Math.min(cc,available);
          if (load > 0.0) {
            pGroup.setLoadType("COL");
            pGroup.setLoad(pGroup.load() + load);
            pPlanet.setCols(pPlanet.cols() - load);
            loaded = load;
          }
        } else if ("CAP".equals(pLoad)) {
          double available = pPlanet.capitals();
          load = Math.min(cc,available);
          if (load > 0.0) {
            pGroup.setLoadType("CAP");
            pGroup.setLoad(pGroup.load() + load);
            pPlanet.setCols(pPlanet.cols() - load);
            loaded = load;
          }
        } else if ("MAT".equals(pLoad)) {
          double available = pPlanet.materials();
          load = Math.min(cc,available);
          if (load > 0.0) {
            pGroup.setLoadType("MAT");
            pGroup.setLoad(pGroup.load() + load);
            pPlanet.setCols(pPlanet.cols() - load);
            loaded = load;
          }
        }
      }
    }
    return loaded;
  }

  static public double unloadOrder(IJG_Group pGroup, IJG_Planet pPlanet, double pAmountToUnLoad) {
    double unloaded = 0.0;
    String type = pGroup.loadType();
    double unload = Math.min(pGroup.load(),pAmountToUnLoad);
    if ("COL".equals(type)) {
      pPlanet.setCols(pPlanet.cols() + unload);
      unloaded = unload;
      if (pPlanet.faction() == null) {
        pPlanet.setFaction(pGroup.faction());
      }
      pGroup.setLoad(pGroup.load() - unload);
      if (pGroup.load() <= 0.0) {
        pGroup.setLoadType(null);
      }
    } else if ("CAP".equals(type)) {
      pPlanet.setCapitals(pPlanet.capitals() + unload);
      unloaded = unload;
      if (pPlanet.faction()==null) {
        pPlanet.setFaction(pGroup.faction());
      }
      pGroup.setLoad(pGroup.load() - unload);
      if (pGroup.load()<=0.0) {
        pGroup.setLoadType(null);
      }
    } else if ("MAT".equals(type)) {
      pPlanet.setMaterials(pPlanet.materials() + unload);
      unloaded = unload;
      if (pPlanet.faction()==null) {
        pPlanet.setFaction(pGroup.faction());
      }
      pGroup.setLoad(pGroup.load() - unload);
      if (pGroup.load()<=0.0) {
        pGroup.setLoadType(null);
      }
    }
    return unloaded;
  }

  private SJG_LoadOrder() {
    return;
  }

}
