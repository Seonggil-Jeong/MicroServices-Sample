package com.food.ordering.system.order.service.domain.event;


import com.food.ordering.system.order.service.domain.entity.Order;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    public OrderPaidEvent(Order order, ZonedDateTime createAt) {
        super(order, createAt);
    }
}