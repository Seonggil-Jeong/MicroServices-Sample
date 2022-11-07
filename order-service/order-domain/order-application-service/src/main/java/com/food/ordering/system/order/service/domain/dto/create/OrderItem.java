package com.food.ordering.system.order.service.domain.dto.create;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class OrderItem {
    @NotNull
    private final UUID productId;

    @NotNull
    private final Integer Quantity;

    @NotNull
    private final BigDecimal price;

    @NotNull
    private final BigDecimal subTotal;
}
