package org.jgalaxy.orders;

import java.util.List;

public record JG_Order(EJG_Order order, List<String> parameters ) implements IJG_Order {
}
