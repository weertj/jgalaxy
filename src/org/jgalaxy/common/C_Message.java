package org.jgalaxy.common;

import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;

public record C_Message(boolean global, String message ) implements IC_Message {


  static public IC_Message of(Node pParent ) {
    return new C_Message(
      Boolean.valueOf(XML_Utils.attr(pParent,"global", "false" )),
      XML_Utils.attr(pParent,"message" )
    );
  }


  @Override
  public void storeObject(File pPath, Node pParent, String pName, String pFilter ) {
    Element message = pParent.getOwnerDocument().createElement("message");
    message.setAttribute("global", "" + global() );
    message.setAttribute("message", message() );
    pParent.appendChild(message);
    return;
  }

}
