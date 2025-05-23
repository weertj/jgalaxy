package org.jgalaxy;

import java.io.Closeable;
import java.util.List;

public interface IEntity extends Closeable {

  @Override
  default void close() {
    return;
  }

  default String entityType() {
    return getClass().getSimpleName();
  }
  String id();
  String name();
  default void setName( String pName ) {
    throw new UnsupportedOperationException();
  }
  default String getName() {
    return name();
  }

  default List<? extends IEntity> subEntities() {
    return List.of();
  }

}
