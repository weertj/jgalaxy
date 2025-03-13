package org.jgalaxy.orders;

import java.util.List;

public interface IJG_Order {

  EJG_Order order();

  String param( int pIx );
  List<String> parameters();

}
