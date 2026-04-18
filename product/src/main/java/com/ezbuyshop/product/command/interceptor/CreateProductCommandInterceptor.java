package com.ezbuyshop.product.command.interceptor;

import java.util.List;
import java.util.function.BiFunction;


import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ezbuyshop.product.command.CreateProductCommand;
import com.ezbuyshop.product.data.ProductLookUpEntity;
import com.ezbuyshop.product.data.ProductLookUpRepository;



@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);
	private final ProductLookUpRepository productLookupRepository;
	
	public CreateProductCommandInterceptor(ProductLookUpRepository productLookupRepository) {
		this.productLookupRepository = productLookupRepository;
	}
 
	@Override
	public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
			List<? extends CommandMessage<?>> messages) {
		 

		return (index, command) -> {
			
			LOGGER.info("Intercepted command: " + command.getPayloadType());
			
			if(CreateProductCommand.class.equals(command.getPayloadType())) {
				
				CreateProductCommand createProductCommand = (CreateProductCommand)command.getPayload();
				
				ProductLookUpEntity productLookupEntity =  productLookupRepository.findByProductIdOrTitle(createProductCommand.getProductId(),
						createProductCommand.getTitle());
				
				if(productLookupEntity != null) {
					throw new IllegalStateException(
							String.format("Product with productId %s or title %s already exist", 
									createProductCommand.getProductId(), createProductCommand.getTitle())
							);
				}

			}
			
			return command;
		};
	}


}
