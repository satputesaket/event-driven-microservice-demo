package com.ezbuyshop.orders.saga;


import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ezbuyshop.core.commands.CancelProductReservedCommand;
import com.ezbuyshop.core.commands.ProcessPaymentCommand;
import com.ezbuyshop.core.commands.ReserveProductCommand;
import com.ezbuyshop.core.events.PaymentProcessedEvent;
import com.ezbuyshop.core.events.ProductReservationCancelEvent;
import com.ezbuyshop.core.events.ProductReservedEvent;
import com.ezbuyshop.core.model.User;
import com.ezbuyshop.core.query.FetchUserPaymentDetailsQuery;
import com.ezbuyshop.orders.command.commands.ApproveOrderCommand;
import com.ezbuyshop.orders.command.commands.RejectOrderCommand;
import com.ezbuyshop.orders.core.events.OrderApprovedEvent;
import com.ezbuyshop.orders.core.events.OrderCreatedEvent;
import com.ezbuyshop.orders.core.events.OrderRejectedEvent;
import com.ezbuyshop.orders.query.FindOrderQuery;
import com.ezbuyshop.orders.core.model.OrderSummary;

@Saga
public class OrderSaga {
	
	//because saga is serialized we are marking command gateway as transient
	@Autowired
	private transient CommandGateway commandGateway;
	
	@Autowired
	private transient QueryGateway queryGateway;

	
	private static final Logger LOGGER = LoggerFactory.getLogger(OrderSaga.class);
	
	@Autowired
	private transient DeadlineManager deadlineManager;
	
	private final String PAYMENT_PROCESSING_TIMEOUT_DEADLINE="payment-processing-deadline";
	
	private String scheduleId;
	
	@Autowired
	private transient QueryUpdateEmitter queryUpdateEmitter;


	
	
	@StartSaga
	@SagaEventHandler(associationProperty="orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
				.orderId(orderCreatedEvent.getOrderId())
				.productId(orderCreatedEvent.getProductId())
				.quantity(orderCreatedEvent.getQuantity())
				.userId(orderCreatedEvent.getUserId())
				.build();
		
		LOGGER.info("OrderCreatedEvent handled for orderId: " 
			    + reserveProductCommand.getOrderId() 
			    + " and productId: " 
			    + reserveProductCommand.getProductId());
		
		commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {

			@Override
			public void onResult(CommandMessage<? extends ReserveProductCommand> commandMessage,
					CommandResultMessage<? extends Object> commandResultMessage) {
				if(commandResultMessage.isExceptional()) {
					
					// Start a compensating transaction
					RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(orderCreatedEvent.getOrderId(),
								commandResultMessage.exceptionResult().getMessage());
						
					commandGateway.send(rejectOrderCommand);
					
				}				
			}
		});
	}
	
	@SagaEventHandler(associationProperty="orderId")
	public void handle(ProductReservedEvent productReservedEvent) {
		LOGGER.info("ProductReservedEvent is called for productId: " 
			    + productReservedEvent.getProductId() 
			    + " and orderId: " 
			    + productReservedEvent.getOrderId());
		
		// process user payment
		
		FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = 
				new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
		
		User userPaymentDetails = null;
		
		try {
		userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, 
				ResponseTypes.instanceOf(User.class)).join();
		
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			cancelProductReservation(productReservedEvent, ex.getMessage());
			return;
		}
		
		if(userPaymentDetails ==null) {
			cancelProductReservation(productReservedEvent, "Cannot process as unable to fetch user details.");
			return;
		}
		LOGGER.info("Successfully fetched user payment details for user " + userPaymentDetails.getFirstName());;
        
	    scheduleId =  deadlineManager.schedule(Duration.of(120, ChronoUnit.SECONDS),
	        		PAYMENT_PROCESSING_TIMEOUT_DEADLINE, productReservedEvent);
	   

		
		ProcessPaymentCommand proccessPaymentCommand = ProcessPaymentCommand.builder()
        		.orderId(productReservedEvent.getOrderId())
        		.paymentDetails(userPaymentDetails.getPaymentDetails())
        		.paymentId(UUID.randomUUID().toString())
        		.build();

//        commandGateway.send(proccessPaymentCommand);
        
        
        String result = null;
        try {
            result = commandGateway.sendAndWait(proccessPaymentCommand, 10, TimeUnit.SECONDS);
        } catch(Exception ex) {
            LOGGER.error(ex.getMessage());
			cancelProductReservation(productReservedEvent, ex.getMessage());
			return;
        }

        if(result == null) {
            LOGGER.info("The ProcessPaymentCommand resulted in NULL. Initiating a compensating transaction");
			cancelProductReservation(productReservedEvent, "The ProcessPaymentCommand resulted in NULL. Initiating a compensating transaction.");
        }		
	}
	
	
	private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {
		cancelDeadline();

		CancelProductReservedCommand cancelProductReservedCommand = CancelProductReservedCommand.builder()
				.orderId(productReservedEvent.getOrderId())
				.productId(productReservedEvent.getProductId())
				.quantity(productReservedEvent.getQuantity())
				.userId(productReservedEvent.getUserId())
				.reason(reason)
				.build();
		
		commandGateway.send(cancelProductReservedCommand);
		
	}
	
	@SagaEventHandler(associationProperty="orderId")
	public void handle(ProductReservationCancelEvent productReservationCancelEvent) {
		RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelEvent.getOrderId(),
				productReservationCancelEvent.getReason());
		
		commandGateway.send(rejectOrderCommand);
	}
	
	@SagaEventHandler(associationProperty="orderId")
	public void handle(PaymentProcessedEvent paymentProcessedEvent) {
		cancelDeadline();
		ApproveOrderCommand approveOrderCommand 
				= new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
		
		commandGateway.send(approveOrderCommand);
		
	}
	
	private void cancelDeadline() {
		if (scheduleId != null) { 
			deadlineManager.cancelSchedule(PAYMENT_PROCESSING_TIMEOUT_DEADLINE, scheduleId);
			scheduleId = null;
		}
	}

	
	@EndSaga
	@SagaEventHandler(associationProperty="orderId")
	public void handle(OrderRejectedEvent orderRejectedEvent) {
		LOGGER.info("Successfully rejected order with id " + orderRejectedEvent.getOrderId());
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, 
				new OrderSummary(orderRejectedEvent.getOrderId(), 
						orderRejectedEvent.getOrderStatus(),
						orderRejectedEvent.getReason()));

	}

	
	
	@EndSaga
	@SagaEventHandler(associationProperty="orderId")
	public void handle(OrderApprovedEvent orderApprovedEvent) {
		LOGGER.info("Order has been Approved " + orderApprovedEvent.getOrderId());;
		queryUpdateEmitter.emit(FindOrderQuery.class, query -> true, 
				new OrderSummary(orderApprovedEvent.getOrderId(), 
						orderApprovedEvent.getOrderStatus(),
						""));


	}
	
	@DeadlineHandler(deadlineName=PAYMENT_PROCESSING_TIMEOUT_DEADLINE)
	public void handlePaymentDeadline(ProductReservedEvent productReservedEvent) {
		LOGGER.info("Payment processing deadline took place. Sending a compensating command to cancel the product reservation");
		cancelProductReservation(productReservedEvent, "Payment timeout");
		
	}

	
	
}
