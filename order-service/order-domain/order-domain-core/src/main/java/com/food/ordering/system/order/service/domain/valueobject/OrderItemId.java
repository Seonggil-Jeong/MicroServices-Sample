package com.food.ordering.system.order.service.domain.valueobject;

import com.food.ordering.system.domain.valueobject.BaseId;

public class OrderItemId extends BaseId<Long> { // orderItemId
    public OrderItemId(Long value) {
        super(value);
    }
}
