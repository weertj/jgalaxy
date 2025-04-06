package org.jgalaxy.units;

import java.util.List;

public class JG_UnitDesigns implements IJG_UnitDesigns {

  static public IJG_UnitDesigns of(List<IJG_UnitDesign> pUnitDesigns) {
    return new JG_UnitDesigns(pUnitDesigns);
  }

  private final List<IJG_UnitDesign> mDesigns;

  private JG_UnitDesigns(List<IJG_UnitDesign> pUnitDesigns) {
    mDesigns = pUnitDesigns;
    return;
  }

  @Override
  public List<IJG_UnitDesign> unitDesigns() {
    return mDesigns;
  }
}
