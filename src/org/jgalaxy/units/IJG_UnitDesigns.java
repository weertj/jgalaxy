package org.jgalaxy.units;

import java.util.List;

public interface IJG_UnitDesigns {

  List<IJG_UnitDesign> unitDesigns();

  default List<IJG_UnitDesign> byPrefix( String pPrefix ) {
    return unitDesigns().stream().filter(ud -> ud.id().startsWith(pPrefix)).toList();
  }

}
