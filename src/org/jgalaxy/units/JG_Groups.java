package org.jgalaxy.units;

import org.jgalaxy.engine.IJG_Faction;

import java.util.ArrayList;
import java.util.List;

public class JG_Groups implements IJG_Groups {

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
    return mGroups;
  }

  @Override
  public void combineGroups() {
    // **** TODO
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
}
