package com.ezbuyshop.orders.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezbuyshop.orders.command.commands.CreateOrderCommand;
import com.ezbuyshop.orders.core.model.OrderStatus;
import com.ezbuyshop.orders.core.model.OrderSummary;
import com.ezbuyshop.orders.query.FindOrderQuery;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderCommandController {

	private final CommandGateway commandGateway;
	private final QueryGateway queryGateway;

	@Autowired
	public OrderCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
		this.commandGateway = commandGateway;
		this.queryGateway = queryGateway;
	}
	
//	@PostMapping
//	public String createOrder(@Valid @RequestBody OrderCreateModel order) {
//		
//		return "Order Id is " + order.getProductId();
//	}

	

	@PostMapping
	public OrderSummary createOrder(@Valid @RequestBody OrderCreateModel order) {

		String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
		String orderId = UUID.randomUUID().toString();

		CreateOrderCommand createOrderCommand = CreateOrderCommand.builder().addressId(order.getAddressId())
				.productId(order.getProductId()).userId(userId).quantity(order.getQuantity()).orderId(orderId)
				.orderStatus(OrderStatus.CREATED).build();

		SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(
				new FindOrderQuery(orderId), ResponseTypes.instanceOf(OrderSummary.class),
				ResponseTypes.instanceOf(OrderSummary.class));

		try {
			commandGateway.sendAndWait(createOrderCommand);
//			return queryResult.updates().blockFirst();
			return queryResult.initialResult().block();
		} finally {
			queryResult.close();
		}

	}


}
