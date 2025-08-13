package com.abhinav.abhinavProject.entity.order;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatusEnum {

    ORDER_PLACED,
    CANCELLED,
    ORDER_REJECTED,
    ORDER_CONFIRMED,
    ORDER_SHIPPED,
    DELIVERED,
    RETURN_REQUESTED,
    RETURN_REJECTED,
    RETURN_APPROVED,
    PICK_UP_INITIATED,
    PICK_UP_COMPLETED,
    REFUND_INITIATED,
    REFUND_COMPLETED,
    CLOSED;

    private Set<OrderStatusEnum> nextStates() {
        return switch (this) {
            case ORDER_PLACED -> EnumSet.of(CANCELLED, ORDER_CONFIRMED, ORDER_REJECTED);
            case CANCELLED, ORDER_REJECTED -> EnumSet.of(REFUND_INITIATED, CLOSED);
            case ORDER_CONFIRMED -> EnumSet.of(CANCELLED, ORDER_SHIPPED);
            case ORDER_SHIPPED -> EnumSet.of(DELIVERED);
            case DELIVERED -> EnumSet.of(RETURN_REQUESTED, CLOSED);
            case RETURN_REQUESTED -> EnumSet.of(RETURN_REJECTED, RETURN_APPROVED);
            case RETURN_REJECTED, REFUND_COMPLETED -> EnumSet.of(CLOSED);
            case RETURN_APPROVED -> EnumSet.of(PICK_UP_INITIATED);
            case PICK_UP_INITIATED -> EnumSet.of(PICK_UP_COMPLETED);
            case PICK_UP_COMPLETED -> EnumSet.of(REFUND_INITIATED);
            case REFUND_INITIATED -> EnumSet.of(REFUND_COMPLETED);
            case CLOSED -> EnumSet.noneOf(OrderStatusEnum.class);
        };
    }

    public boolean canTransitionTo(OrderStatusEnum toState) {
        return this.nextStates().contains(toState);
    }

}
