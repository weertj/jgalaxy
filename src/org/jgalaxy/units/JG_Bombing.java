package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;

public record JG_Bombing( String who, String whichGroup, IJG_Position position ) implements IJG_Bombing {

  static public IJG_Bombing of( Node pParent ) {
    return new JG_Bombing(
      XML_Utils.attr(pParent,"who" ),
      XML_Utils.attr(pParent,"whichGroup" ),
      JG_Position.of(Double.parseDouble(XML_Utils.attr(pParent,"x" )),Double.parseDouble(XML_Utils.attr(pParent,"y" )))
    );
  }

  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element bombing = pParent.getOwnerDocument().createElement("bombing");
    bombing.setAttribute("who", who() );
    bombing.setAttribute("whichGroup", whichGroup() );
    bombing.setAttribute("x", ""+position().x() );
    bombing.setAttribute("y", ""+position().y() );
    pParent.appendChild(bombing);
    return;
  }

}
