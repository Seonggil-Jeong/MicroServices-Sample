package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import org.springframework.validation.annotation.Validated;

public interface OrderApplicationService {

    CreateOrderResponse createOrder(@Validated CreateOrderCommand createOrderCommand);

    TrackOrderResponse trackOrder(@Validated TrackOrderQuery trackOrderQuery);

}
