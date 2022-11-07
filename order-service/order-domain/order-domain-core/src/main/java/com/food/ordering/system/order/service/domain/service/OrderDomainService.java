package com.food.ordering.system.order.service.domain.service;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(final Order order, Restaurant restaurant);

    OrderPaidEvent payOrder(final Order order);

    void approveOrder(final Order order);

    OrderCancelledEvent cancelOrderPayment(final Order order, List<String> failureMessages);

    void cancelOrder(final Order order, List<String> failureMessage);
}
