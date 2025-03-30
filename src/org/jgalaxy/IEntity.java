package org.jgalaxy;

import java.util.List;

public interface IEntity {

  String id();
  String name();

  default List<? extends IEntity> subEntities() {
    return List.of();
  }

}
