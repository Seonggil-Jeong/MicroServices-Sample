package com.food.ordering.system.order.service.domain.ports.output.repository.message.publisher.payment;

import com.food.ordering.system.domain.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatePaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
