package com.food.ordering.system.order.service.domain.service.impl;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.service.domain.excepton.OrderDomainException;
import com.food.ordering.system.order.service.domain.service.OrderDomainService;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant) {
        validateRestaurant(restaurant);
        setOrderProductInfo(order, restaurant);

        order.validateOrder();
        order.initializeOrder();

        log.info("Order with id : {} is initiated", order.getId().getValue());

        return new OrderCreatedEvent(order, zonedDateTimeInSeoul());
    }

    private void setOrderProductInfo(final Order order, final Restaurant restaurant) {
        order.getItems().forEach(orderItem -> restaurant.getProducts().forEach(restaurantProduct -> {
            Product currentProduct = orderItem.getProduct();

            if (currentProduct.equals(restaurantProduct)) {
                currentProduct.updateWithConfirmedNameAndPrice(restaurantProduct.getName(), restaurantProduct.getPrice());
            }
        }));
    }

    private void validateRestaurant(final Restaurant restaurant) {
        if (!restaurant.isActive()) {
            throw new OrderDomainException("Restaurant with id : " + restaurant.getId().getValue());
        }

    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();
        log.info("Order with id : {} is paid", order.getId().getValue());

        return new OrderPaidEvent(order, zonedDateTimeInSeoul());
    }

    @Override
    public void approveOrder(Order order) {
        order.approved();
        log.info("Order with id : {} is approved", order.getId().getValue());
    }

    @Override
    public OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages) {
        order.initCancel(failureMessages);
        log.info("Order payment is cancelling for order Id : {}", order.getId().getValue());

        return new OrderCancelledEvent(order, zonedDateTimeInSeoul());
    }

    @Override
    public void cancelOrder(Order order, List<String> failureMessage) {
        order.cancel(failureMessage);
        log.info("Order with id : {} is cancelled", order.getId().getValue());

    }


    /**
     * get zoneDateTime By ZoneId(Seoul)
     * @return ZonedDateTime
     */
    private ZonedDateTime zonedDateTimeInSeoul() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    }
}
