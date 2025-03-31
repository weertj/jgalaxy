package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;

public record JG_Incoming( IJG_Position from, IJG_Position current, IJG_Position to, Double mass ) implements IJG_Incoming{


  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element incoming = pParent.getOwnerDocument().createElement("incoming");
    incoming.setAttribute("from-x", ""+from().x() );
    incoming.setAttribute("from-y", ""+from().y() );
    incoming.setAttribute("current-x", ""+current().x() );
    incoming.setAttribute("current-y", ""+current().y() );
    incoming.setAttribute("to-x", ""+to().x() );
    incoming.setAttribute("to-y", ""+to().y() );
    incoming.setAttribute("mass", ""+mass() );
    pParent.appendChild(incoming);
    return;
  }

}
