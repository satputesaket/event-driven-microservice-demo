package com.ezbuyshop.product.command.event;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.ezbuyshop.product.data.ProductLookUpEntity;
import com.ezbuyshop.product.data.ProductLookUpRepository;

@Component
@ProcessingGroup("product-group")
public class ProductLookUpEventHandler {
	
	private ProductLookUpRepository productLookUpRepository;
	
	
	public ProductLookUpEventHandler(ProductLookUpRepository productLookUpRepository) {
		this.productLookUpRepository = productLookUpRepository;
	}

	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {


		ProductLookUpEntity productLookUpEntity = new ProductLookUpEntity(productCreatedEvent.getProductId(),
				productCreatedEvent.getTitle());

		productLookUpRepository.save(productLookUpEntity);
		


	}

}
