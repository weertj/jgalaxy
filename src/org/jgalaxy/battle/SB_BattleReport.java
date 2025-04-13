package org.jgalaxy.battle;

import org.jgalaxy.Entity;
import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.units.IJG_Group;

import java.util.ArrayList;
import java.util.List;

public class SB_BattleReport extends Entity implements ISB_BattleReport {

  static public ISB_BattleReport of(IJG_Faction pFaction, IJG_Position pPosition) {
    return new SB_BattleReport(pFaction, pPosition);
  }

  private final IJG_Faction   mFaction;
  private final IJG_Position  mPosition;

  private SB_BattleReport(IJG_Faction pFaction, IJG_Position pPosition) {
    super("battlereport-" + pPosition.x() + "," + pPosition.y(), "battlereport-" + pPosition.x() + "," + pPosition.y() );
    mFaction = pFaction;
    mPosition = pPosition;
    return;
  }

  @Override
  public boolean isInvolved(IJG_Faction pFaction) {
    return mFaction.groups().groupsByPosition(mPosition).getGroups()
      .stream()
      .anyMatch( g -> !g.shotsMutable().isEmpty());
  }

  @Override
  public List<IJG_Group> groups() {
    List<IJG_Group> groups = new ArrayList<>(8);
    //var planet = mFaction.planets().findPlanetByPosition(mPosition);
    groups.addAll(mFaction.groups().groupsByPosition(mPosition)
      .getGroups().stream()
      .filter(g -> !g.shotsMutable().isEmpty())
      .toList()
    );
    for( var faction : mFaction.getOtherFactionsMutable()) {
      groups.addAll(faction.groups().groupsByPosition(mPosition)
        .getGroups().stream()
        .filter(g -> !g.shotsMutable().isEmpty())
        .toList()
      );
    }
    return groups;
  }

}
