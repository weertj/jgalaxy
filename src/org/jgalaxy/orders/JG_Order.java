package org.jgalaxy.orders;

import org.jgalaxy.utils.XML_Utils;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record JG_Order(EJG_Order order, List<String> parameters ) implements IJG_Order {

  static public IJG_Order of( Node pParent ) {
    EJG_Order order = EJG_Order.valueOf(XML_Utils.attr(pParent,"order"));
    List<String> parameters = new ArrayList<>();
    for( int ix=0; ix<8; ix++ ) {
      parameters.add(XML_Utils.attr(pParent,"param" + ix ));
    }
    return new JG_Order( order, parameters );
  }

  @Override
  public String param(int pIx) {
    if (pIx<parameters().size()) {
      return parameters().get(pIx);
    }
    return "";
  }

  @Override
  public String toString() {
    return order.name() +  parameters.stream().collect(Collectors.joining(" "," ",""));
  }
}
