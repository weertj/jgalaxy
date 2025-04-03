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
  private       int                           mBattleRound = 0;

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

  private boolean hasDestroyableTarget( IJG_UnitDesign pAttackerDesign, IJG_Group pAttacker, IJG_Groups pGroups ) {
    if (pAttacker.getNumberOf()>0) {
      var attfaction = mGame.getFactionById(pAttacker.faction());
      double weapon = pAttackerDesign.effectiveWeapon(pAttacker.tech());
      for (IJG_Group group : pGroups.getGroups()) {
        if (group.getNumberOf()>0 && attfaction.atWarWith().contains(group.faction())) {
          var deffaction = mGame.getFactionById(group.faction());
          var udef = deffaction.getUnitDesignById(group.unitDesign());
          double shields = udef.effectiveShield(group.tech());
          if (killChance(weapon,shields)>0) {
            return true;
          }
        }
      }
    }
    return false;
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

  private double killChance( double pWeapon, double pShields ) {
    return ((Math.log(pWeapon/pShields)/Math.log(4))+1)/2;
  }


  /**
   * attack
   * @param pAttackerDesign
   * @param pAttacker
   * @param pDefFaction
   * @param pDefenderDesign
   * @param pDefender
   * @return
   */
  private Boolean attack(IJG_UnitDesign pAttackerDesign,IJG_Group pAttacker, IJG_Faction pDefFaction, IJG_UnitDesign pDefenderDesign, IJG_Group pDefender ) {

    var defdesign = pDefFaction.getUnitDesignById(pDefender.unitDesign());
    double mass = defdesign.mass() + pDefender.totalCargoMass()/pDefender.getNumberOf();
//    double shields = (pDefender.tech().shields() * pDefenderDesign.shields() / Math.pow(mass,0.3333333)) * 3.10723250595;
    double shields = pDefenderDesign.effectiveShield( pDefender.tech());
    if (shields>0) {
      double weapon = pAttackerDesign.effectiveWeapon( pAttacker.tech());
//      double weapon = pAttacker.tech().weapons() * pAttackerDesign.weapons();
//      double pkill = ((Math.log(weapon/shields)/Math.log(4))+1)/2;
      double pkill = killChance(weapon/shields, mass);
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
      var uatt = faction.getUnitDesignById(attacker.unitDesign());
      if (hasDestroyableTarget( uatt, attacker, groups )) {
        rerun = true;
        var atwar = faction.atWarWith();
        for (int gun = 0; gun < uatt.nrweapons() * attacker.getNumberOf(); gun++) {
          var targets = groups.groupsByFactions(atwar);
          if (!targets.isEmpty()) {
            var target = selectTargetGroup(targets);
            if (target != null) {
              IJG_Faction deffaction = mGame.getFactionById(target.faction());
              var udef = deffaction.getUnitDesignById(target.unitDesign());
              Boolean result = attack(uatt, attacker, deffaction, udef, target);
              if (result == null) {
                // **** No chance to kill
                attacker.shotsMutable().add(new B_Shot(IB_Shot.TYPE.SHIP_SHIP, mBattleRound, target.id(), target.faction(), -1));
              } else {
                if (result) {
                  // **** Hit
                  attacker.shotsMutable().add(new B_Shot(IB_Shot.TYPE.SHIP_SHIP, mBattleRound, target.id(), target.faction(), 1));
                  target.setNumberOf(target.getNumberOf() - 1);
                  if (target.getNumberOf() <= 0) {
                    // **** Destroyed
//                  deffaction.groups().removeGroup(target);
                  }
                } else {
                  // **** Shields
                  attacker.shotsMutable().add(new B_Shot(IB_Shot.TYPE.SHIP_SHIP, mBattleRound, target.id(), target.faction(), 0));
                }
              }
            }
          }
        }
      }
    }
    mBattleRound++;
    return rerun;
  }
}
