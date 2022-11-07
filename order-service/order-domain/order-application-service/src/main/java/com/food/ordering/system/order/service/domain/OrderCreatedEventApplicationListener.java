package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.ports.output.repository.message.publisher.payment.OrderCreatePaymentRequestMessagePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCreatedEventApplicationListener {
    private final OrderCreatePaymentRequestMessagePublisher orderCreatePaymentRequestMessagePublisher;

    // 트랜잭션 메서드에서 발생하는 이벤트를 수신
    // 트랜잭션 작업이 성공적으로 완료된 경우에만 이벤트를 처리
    @TransactionalEventListener
    void process(OrderCreatedEvent orderCreatedEvent) {
        orderCreatePaymentRequestMessagePublisher.publish(orderCreatedEvent);

    }
}
