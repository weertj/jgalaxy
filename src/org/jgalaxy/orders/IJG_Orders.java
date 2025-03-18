package org.jgalaxy.orders;

import java.util.ArrayList;
import java.util.List;

public interface IJG_Orders {

  void addOrder(IJG_Order order);
  List<IJG_Order> orders();

  default List<IJG_Order> ordersBy( EJG_Order... pOrderType ) {
    List<IJG_Order> orders = new ArrayList<>();
    for( EJG_Order order : pOrderType ) {
      orders.addAll( orders().stream().filter( o -> o.order()==order ).toList());
    }
    return orders;
  }


}
