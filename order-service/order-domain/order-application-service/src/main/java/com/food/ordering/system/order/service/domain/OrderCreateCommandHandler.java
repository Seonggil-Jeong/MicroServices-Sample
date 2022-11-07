package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.excepton.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import com.food.ordering.system.order.service.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateCommandHandler {
    private final OrderCreateHandler orderCreateHandler;
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;
    private final OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher;
    private final ApplicationDomainEventPublisher applicationDomainEventPublisher;

    @Transactional(rollbackFor = Exception.class)
    public CreateOrderResponse createOrder(final CreateOrderCommand command) {
        final OrderCreatedEvent orderCreatedEvent = orderCreateHandler.persistOrder(command);
        log.info("Order is created with id : {}", orderCreatedEvent.getOrder().getId().getValue());

        applicationDomainEventPublisher.publish(orderCreatedEvent);

        orderCreatePaymentRequestMessagePublisher.publish(orderCreatedEvent); // send event to kafka
        return orderDataMapper.orderToCreateOrderResponse(orderCreatedEvent.getOrder());
    }
}
