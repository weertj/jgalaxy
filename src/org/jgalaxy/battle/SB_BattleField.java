package org.jgalaxy.battle;

import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.units.IJG_Group;
import org.jgalaxy.units.IJG_Groups;
import org.jgalaxy.units.IJG_UnitDesign;
import org.jgalaxy.units.JG_Groups;
import org.jgalaxy.utils.GEN_Math;

import java.util.*;
import java.util.stream.Collectors;

public class SB_BattleField implements ISB_BattleField {

  static public ISB_BattleField of( IJG_Game pGame ) {
    return new SB_BattleField(pGame);
  }

  private final IJG_Game                      mGame;
  private final Map<IJG_Faction, IJG_Groups>  mField = new HashMap<>(64);
  private final Map<IJG_Faction, IJG_Groups>  mDoneField = new HashMap<>(64);

  private SB_BattleField( IJG_Game pGame ) {
    mGame = pGame;
    return;
  }

  @Override
  public boolean isBattle() {
    return mField.size()>1;
  }

  @Override
  public void addEntry(IJG_Faction pFaction, IJG_Group pGroup) {
    IJG_Groups groups = mField.get(pFaction);
    if (groups==null) {
      groups = JG_Groups.of();
    }
    groups.addGroup(pGroup);
    mField.put(pFaction, groups);
    return;
  }

  @Override
  public void addEntry(IJG_Faction pFaction, IJG_Groups pGroups) {
    pGroups.getGroups().stream().forEach(g->addEntry(pFaction,g));
    return;
  }

  private void setupForRound() {
    mDoneField.clear();
    return;
  }

  private IJG_Group selectTargetGroup( IJG_Groups pGroups ) {
    int totalNumberOfTargets = pGroups.totalNumberOfUnits();
    if (totalNumberOfTargets==0) {
      return null;
    }
    int n = GEN_Math.math().randomRange(0,totalNumberOfTargets-1);
    IJG_Group attackgroup = pGroups.getGroupByIndex(n);
    return attackgroup;
  }

  private Boolean attack(IJG_UnitDesign pAttackerDesign,IJG_Group pAttacker, IJG_Faction pDefFaction, IJG_UnitDesign pDefenderDesign, IJG_Group pDefender ) {

    var defdesign = pDefFaction.getUnitDesignById(pDefender.unitDesign());
    double mass = defdesign.mass() + pDefender.totalCargoMass()/pDefender.getNumberOf();
    double shields = (pDefender.tech().shields() * pDefenderDesign.shields() / Math.pow(mass,0.3333333)) * 3.10723250595;
    if (shields>0) {
      double weapon = pAttacker.tech().weapons() * pAttackerDesign.weapons();
      double pkill = ((Math.log(weapon/shields)/Math.log(4))+1)/2;
      if (pkill<0) {
        return null;
      }
      return Boolean.valueOf(pkill>GEN_Math.math().nextRandom());
    } else {
      return Boolean.TRUE;
    }
  }

  @Override
  public boolean battleRound() {
    boolean rerun = false;
    setupForRound();

    IJG_Groups groups = JG_Groups.of(mField.values().stream().flatMap(g -> g.getGroups().stream()).collect(Collectors.toList()));
    groups.shuffle();
    for( IJG_Group attacker : groups.getGroups() ) {
      IJG_Faction faction = mGame.getFactionById(attacker.faction());
      var atwar = faction.atWarWith();
      var uatt = faction.getUnitDesignById(attacker.unitDesign());
      for( int gun=0; gun<uatt.nrweapons(); gun++ ) {
        var targets = groups.groupsByFactions(atwar);
        if (!targets.isEmpty()) {
          var target = selectTargetGroup(targets);
          if (target!=null) {
            IJG_Faction deffaction = mGame.getFactionById(target.faction());
            var udef = deffaction.getUnitDesignById(target.unitDesign());
            Boolean result = attack(uatt, attacker, deffaction, udef, target);
            if (result==null) {
            } else {
              rerun = true;
              if (result) {
                // **** Hit
                target.setNumberOf(target.getNumberOf() - 1);
                if (target.getNumberOf() <= 0) {
                  // **** Destroyed
                  deffaction.groups().removeGroup(target);
                }
              }
            }
          }
        }
      }
    }

    return rerun;
  }
}
