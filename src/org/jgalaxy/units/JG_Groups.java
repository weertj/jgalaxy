package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JG_Groups implements IJG_Groups {

  static public IJG_Groups of(List<IJG_Group> pGroups) {
    IJG_Groups groups = new JG_Groups();
    pGroups.stream().forEach(groups::addGroup);
    return groups;
  }

  static public IJG_Groups of() {
    return new JG_Groups();
  }

  private final List<IJG_Group> mGroups = new ArrayList<>(64);

  private JG_Groups() {
    return;
  }

  @Override
  public IJG_Group getGroupById(String pGroupId) {
    return mGroups.stream().filter(g->g.id().equals(pGroupId)).findFirst().orElse(null);
  }

  @Override
  public void addGroup(IJG_Group pGroup) {
    mGroups.add( pGroup );
    return;
  }

  @Override
  public List<IJG_Group> getGroups() {
    return new ArrayList<>(mGroups);
  }

  @Override
  public IJG_Groups groupsByPosition(IJG_Position pPosition) {
    return of( mGroups.stream().filter(g->g.position().equals(pPosition)).collect(Collectors.toList()));
  }

  @Override
  public IJG_Groups groupsByFaction(IJG_Faction pFaction) {
    return of( mGroups.stream().filter(g->g.faction().equals(pFaction.id())).collect(Collectors.toList()));
  }

  @Override
  public IJG_Groups groupsByFactions(List<String> pFactions) {
    return of( mGroups.stream().filter(g-> pFactions.contains(g.faction())).collect(Collectors.toList()));
  }

  @Override
  public void combineGroups() {
    boolean rerun = true;
    while (rerun) {
      rerun = false;
      for (IJG_Group group : new ArrayList<>(mGroups)) {
        for (IJG_Group innergroup : new ArrayList<>(mGroups)) {
          if (group != innergroup &&
            group.position().equals(innergroup.position()) &&
            group.tech().equals(innergroup.tech())) {
            group.setNumberOf(group.numberOf() + innergroup.numberOf());
            mGroups.remove(innergroup);
            rerun = true;
            break;
          }
        }
        if (rerun) {
          break;
        }
      }
    }
    return;
  }

  @Override
  public void shuffle() {
    Collections.shuffle(mGroups);
    return;
  }

  @Override
  public int totalNumberOfUnits() {
    return mGroups.stream().mapToInt( IJG_Group::numberOf ).sum();
  }

  @Override
  public IJG_Group getGroupByIndex(int pIndex) {
    int ix = 0;
    for( IJG_Group group : mGroups ) {
      ix += group.numberOf();
      if (pIndex<ix) {
        return group;
      }
    }
    return null;
  }

  @Override
  public void removeGroup(IJG_Group pGroup) {
    mGroups.remove(pGroup);
    return;
  }

  @Override
  public void moveGroups(IJG_Faction pFaction) {
    for( IJG_Group group : mGroups ) {
      if (group.getFleet()!=null) {
        // **** TODO
        throw new UnsupportedOperationException();
      } else {
        IJG_UnitDesign unitdesign = pFaction.getUnitDesignById(group.unitDesign());
        double speed = unitdesign.speed(group.tech()); // TODO (cargo)

        double dx = group.toPosition().x() - group.position().x();
        double dy = group.toPosition().y() - group.position().y();
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance <= speed) {
          group.position().copyOf(group.toPosition());
        } else {
          double directionX = dx / distance;
          double directionY = dy / distance;
          double newX = group.position().x() + directionX * speed;
          double newY = group.position().y() + directionY * speed;
          group.position().setX(newX);
          group.position().setY(newY);
        }
      }
    }
    return;
  }

  @Override
  public String toString() {
    return "JG_Groups{" +
      "mGroups=" + mGroups +
      '}';
  }
}
