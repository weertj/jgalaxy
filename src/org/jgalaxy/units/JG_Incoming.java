package org.jgalaxy.units;

import org.jgalaxy.IJG_Position;
import org.jgalaxy.JG_Position;
import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;

public record JG_Incoming( IJG_Position from, IJG_Position current, IJG_Position to, Double mass ) implements IJG_Incoming{

  static public IJG_Incoming of( Node pParent ) {
    return new JG_Incoming(
      JG_Position.of(Double.parseDouble(XML_Utils.attr(pParent,"from-x" )),Double.parseDouble(XML_Utils.attr(pParent,"from-y" ))),
      JG_Position.of(Double.parseDouble(XML_Utils.attr(pParent,"current-x" )),Double.parseDouble(XML_Utils.attr(pParent,"current-y" ))),
      JG_Position.of(Double.parseDouble(XML_Utils.attr(pParent,"to-x" )),Double.parseDouble(XML_Utils.attr(pParent,"to-y" ))),
      Double.parseDouble(XML_Utils.attr(pParent,"mass" ))
    );
  }

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
