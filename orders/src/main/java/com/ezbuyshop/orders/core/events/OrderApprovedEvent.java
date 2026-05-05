package com.ezbuyshop.orders.core.events;

import com.ezbuyshop.orders.core.model.OrderStatus;

import lombok.Value;

@Value
public class OrderApprovedEvent {

	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;
	
}