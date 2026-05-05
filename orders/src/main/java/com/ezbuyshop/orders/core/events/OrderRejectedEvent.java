package com.ezbuyshop.orders.core.events;

import com.ezbuyshop.orders.core.model.OrderStatus;

import lombok.Value;

@Value
public class OrderRejectedEvent {
	private final String orderId;
	private final String reason;
	private final OrderStatus orderStatus = OrderStatus.REJECTED;
}