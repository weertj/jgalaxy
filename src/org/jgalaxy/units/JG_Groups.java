package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.engine.IJG_Faction;
import org.jgalaxy.engine.IJG_Game;
import org.jgalaxy.map.IMAP_Map;
import org.jgalaxy.planets.IJG_Planet;

import java.util.*;
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

  private final List<IJG_Fleet> mFleets = new ArrayList<>(64);
  private final List<IJG_Group> mGroups = new ArrayList<>(64);

  private JG_Groups() {
    return;
  }

  @Override
  public IJG_Group getGroupById(String pGroupId) {
    return mGroups.stream().filter(g->g.id().equals(pGroupId)).findFirst().orElse(null);
  }

  @Override
  public int getSize() {
    return mGroups.size();
  }

  @Override
  public void addGroup(IJG_Group pGroup) {
    if (!mGroups.contains(pGroup)) {
      mGroups.add(pGroup);
    }
    return;
  }

  @Override
  public void addGroupAlways(IJG_Group pGroup) {
    mGroups.add(pGroup);
    return;
  }

  @Override
  public List<IJG_Group> getGroups() {
    return new ArrayList<>(mGroups);
  }

  @Override
  public boolean isGroupAtPosition(IJG_Position pPosition) {
    return mGroups.stream().filter(g->g.position().equals(pPosition)).findAny().isPresent();
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
            group.unitDesign().equals(innergroup.unitDesign()) &&
            group.tech().equals(innergroup.tech()) &&
            Objects.equals(group.loadType(),innergroup.loadType()) &&
            Objects.equals(group.getFleet(),innergroup.getFleet())
          ) {
            group.setNumberOf(group.getNumberOf() + innergroup.getNumberOf());
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
    return mGroups.stream().mapToInt( IJG_Group::getNumberOf).sum();
  }

  @Override
  public IJG_Group getGroupByGroupIndex(int pIndex) {
    return mGroups.get( pIndex );
  }

  @Override
  public IJG_Group getGroupByIndex(int pIndex) {
    int ix = 0;
    for( IJG_Group group : mGroups ) {
      ix += group.getNumberOf();
      if (pIndex<ix) {
        return group;
      }
    }
    return null;
  }

  @Override
  public IJG_Fleet addFleet(String pID, String pName) {
    IJG_Fleet fleet = getFleetByName( pName );
    if (fleet==null) {
      fleet = JG_Fleet.of( pID,pName,new ArrayList<>());
      mFleets.add( fleet );
    }
    return fleet;
  }

  @Override
  public IJG_Fleet getFleetByName(String pName) {
    if (pName==null) {
      return null;
    }
    return fleets().stream().filter(f->f.name().equals(pName)).findFirst().orElse(null);
  }

  @Override
  public List<IJG_Fleet> fleets() {
    List<IJG_Fleet> fleets = new ArrayList<>(8);
    var fleetnames = mGroups.stream()
      .map( g -> g.getFleet() )
      .filter( Objects::nonNull )
      .distinct().toList();
    for( String fleetname : fleetnames ) {
      fleets.add(JG_Fleet.of(fleetname,fleetname, mGroups.stream()
        .filter(group -> Objects.equals(group.getFleet(),fleetname)).toList()));
    }
    for( var fleet : mFleets ) {
      if (!fleets.contains(fleet)) {
        fleets.add(fleet);
      }
    }
    return fleets;
  }

  @Override
  public List<IJG_Fleet> fleetsByPosition(IJG_Position pPosition) {
    return fleets().stream()
      .filter(f-> f.position()!=null && f.position().equals(pPosition))
      .toList();
  }

  @Override
  public void removeGroup(IJG_Group pGroup) {
    mGroups.remove(pGroup);
    return;
  }

  /**
   * moveGroup
   * @param pFaction
   * @param pGroup
   * @param pMaxSpeed
   */
  private void moveGroup(IJG_Faction pFaction,IJG_Group pGroup, Double pMaxSpeed) {
//    IJG_UnitDesign unitdesign = pFaction.getUnitDesignById(pGroup.unitDesign());
    double speed = pGroup.maxSpeed(pFaction);
//    double speed = unitdesign.speed(pGroup.tech()); // TODO (cargo)
    if (pMaxSpeed!=null) {
      speed = Math.min(speed,pMaxSpeed);
    }

    double dx = pGroup.toPosition().x() - pGroup.position().x();
    double dy = pGroup.toPosition().y() - pGroup.position().y();
    double distance = Math.sqrt(dx * dx + dy * dy);
    if (distance <= speed) {
      pGroup.position().copyOf(pGroup.toPosition());
      pGroup.lastStaticPosition().copyOf(pGroup.toPosition());
    } else {
      double directionX = dx / distance;
      double directionY = dy / distance;
      double newX = pGroup.position().x() + directionX * speed;
      double newY = pGroup.position().y() + directionY * speed;
      pGroup.position().setX(newX);
      pGroup.position().setY(newY);
    }
    return;
  }

  /**
   * moveGroups
   * @param pGame
   * @param pFaction
   */
  @Override
  public void moveGroups(IJG_Game pGame,IJG_Faction pFaction) {
    // **** Move fleets
    for( IJG_Fleet fleet : fleets()) {
      double maxspeed = fleet.maxSpeed(pFaction);
      for( IJG_Group group : fleet.groups() ) {
        moveGroup(pFaction, group, maxspeed);
      }
    }
    // **** Move groups
    for( IJG_Group group : mGroups ) {
      if (group.getFleet()==null) {
        moveGroup(pFaction, group, null);
      }
    }

    // **** Add groups to incoming groups
    for( IJG_Faction faction : pGame.factions() ) {
      // **** the fleets
      for( IJG_Fleet fleet : fleets() ) {
        if (!fleet.groups().isEmpty()) {
          IJG_Group group = fleet.groups().getFirst();
          if (!group.position().equals(group.toPosition())) {
            IJG_Planet toplanet   = faction.planets().findPlanetByPosition(group.toPosition());
            IJG_Planet fromplanet = faction.planets().findPlanetByPosition(group.lastStaticPosition());
            if (toplanet!=null && fromplanet!=null && toplanet.faction()!=null && !toplanet.faction().equals(group.faction())) {
              IJG_Incoming incoming = new JG_Incoming( group.lastStaticPosition(), group.position(), group.toPosition(), group.totalMass(pGame.getFactionById(group.faction())) );
              var tofact = pGame.getFactionById(toplanet.faction());
              if (tofact!=null) {
                tofact.getIncomingMutable().add(incoming);
              }
            }
          }
        }
      }
      // **** the groups
      IMAP_Map map = pGame.galaxy().map();
      for( IJG_Group group : getGroups() ) {
        if (group.getFleet()==null && !group.position().equals(group.toPosition())) {
          IJG_Planet toplanet   = map.planets().findPlanetByPosition(group.toPosition());
          IJG_Planet fromplanet = map.planets().findPlanetByPosition(group.lastStaticPosition());
          if (toplanet!=null && fromplanet!=null && toplanet.faction()!=null && !toplanet.faction().equals(group.faction())) {
            IJG_Incoming incoming = new JG_Incoming( group.lastStaticPosition(), group.position(), group.toPosition(), group.totalMass(pGame.getFactionById(group.faction())) );
            var tofact = pGame.getFactionById(toplanet.faction());
            if (tofact!=null) {
              tofact.getIncomingMutable().add(incoming);
            }
//            pGame.getFactionById(toplanet.faction()).getIncomingMutable().add(incoming);
          }
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
