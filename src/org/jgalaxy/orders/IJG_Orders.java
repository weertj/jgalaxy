package org.jgalaxy.orders;

import java.util.List;

public interface IJG_Orders {


  void addOrder(IJG_Order order);
  List<IJG_Order> orders();

}
