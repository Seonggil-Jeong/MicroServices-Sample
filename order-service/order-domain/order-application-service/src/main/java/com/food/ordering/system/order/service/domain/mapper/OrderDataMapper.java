package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Restaurant createOrderCommandToRestaurant(CreateOrderCommand command) {
        return Restaurant.Builder.builder()
                .restaurantId(new RestaurantId(command.getRestaurantId()))
                .products(command.getItems().stream().map(orderItem ->
                                new Product(new ProductId(orderItem.getProduct().getId().getValue())))
                        .collect(Collectors.toList())).build();
    }

    public Order createOrderCommandToOrder(CreateOrderCommand command) {
        return Order.Builder.builder()
                .customerId(new CustomerId(command.getCustomerId()))
                .restaurantId(new RestaurantId(command.getRestaurantId()))
                .deliveryAddress(orderAddressToStreetAddress(command.getAddress()))
                .price(new Money(command.getPrice()))
                .items(orderItemsToOrderItemEntities(command.getItems())).build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items.stream().map(orderItem ->
                OrderItem.Builder.builder()
                        .product(new Product(new ProductId(orderItem.getProduct().getId().getValue())))
                        .price(orderItem.getPrice())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal()).build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
        return new StreetAddress(UUID.randomUUID(), address.getStreet(), address.getPostalCode(), address.getCity());
    }

    public CreateOrderResponse orderToCreateOrderResponse(Order order) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus()).build();
    }
}
