package com.ezbuyshop.users.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UsersEventsHandler {
	
	  @QueryHandler
	    public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
	        
	        PaymentDetails paymentDetails = PaymentDetails.builder()
	                .cardNumber("123Card")
	                .cvv("123")
	                .name("SERGEY KARGOPOLOV")
	                .validUntilMonth(12)
	                .validUntilYear(2030)
	                .build();

	        User user = User.builder()
	                .firstName("Sergey")
	                .lastName("Kargopolov")
	                .userId(query.getUserId())
	                .paymentDetails(paymentDetails)
	                .build();

	        return user;
	    }

}
