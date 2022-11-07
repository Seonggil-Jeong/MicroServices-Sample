package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.excepton.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import com.food.ordering.system.order.service.domain.service.OrderDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreateHandler {
    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderCreatedEvent persistOrder(final CreateOrderCommand command) {
        checkCustomer(command.getCustomerId());

        final Restaurant restaurant = checkRestaurant(command);
        final Order order = orderDataMapper.createOrderCommandToOrder(command);

        OrderCreatedEvent event = orderDomainService.validateAndInitiateOrder(order, restaurant);

        log.info("Order is created with id {}", event.getOrder().getId().getValue());

        return event;

    }


    private Restaurant checkRestaurant(CreateOrderCommand command) {
        final Optional<Restaurant> restaurant = restaurantRepository
                .findRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(command));

        if (!restaurant.isPresent()) {
            log.warn("Could not find restaurant with restaurant id : {}", command.getRestaurantId());
            throw new OrderDomainException("Could not find restaurant with restaurant id : " +
                    command.getRestaurantId());
        }

        return restaurant.get();
    }

    private void checkCustomer(UUID customerId) {
        Optional<Customer> customer = customerRepository.findCustomer(customerId);

        if (!customer.isPresent()) {
            log.warn("Could not find customer with customer id : {}", customerId);
            throw new OrderDomainException("Could not find customer with customer id : " + customerId);
        }
    }

    private Order saveOrder(final Order order) {
        Order result = orderRepository.save(order);

        if (result == null) {
            log.info("save order Error");
            throw new OrderDomainException("Could not save order");
        }
        log.info("Order is saved with id : {}", result.getId().getValue());
        return result;
    }

}
