package com.ezbuyshop.product.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ezbuyshop.core.events.ProductReservationCancelEvent;
import com.ezbuyshop.core.events.ProductReservedEvent;
import com.ezbuyshop.product.command.event.ProductCreatedEvent;
import com.ezbuyshop.product.data.ProductEntity;
import com.ezbuyshop.product.data.ProductRepository;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {
	
	private final ProductRepository productRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventHandler.class);

	
	public ProductEventHandler(ProductRepository productRepository) {
		this.productRepository=productRepository;
	}
	
	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		ProductEntity productEntity = new ProductEntity();		
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
		productRepository.save(productEntity);
	}
	
	@EventHandler
	public void on(ProductReservedEvent productReservedEvent) {
		ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());
		productEntity.setQuantity(productEntity.getQuantity()-productReservedEvent.getQuantity());
		productRepository.save(productEntity);
		
		LOGGER.info("ProductReservedEvent is called for productId: " 
			    + productReservedEvent.getProductId() 
			    + " and orderId: " 
			    + productReservedEvent.getOrderId());
		
	}
	
	@EventHandler
	public void on(ProductReservationCancelEvent productReservationCancelEvent) {
		
		ProductEntity productEntity = productRepository.findByProductId(productReservationCancelEvent.getProductId());
		int newProductQuantity= productEntity.getQuantity() + productReservationCancelEvent.getQuantity();
		productEntity.setQuantity(newProductQuantity);
		productRepository.save(productEntity);
		
		
	}

}
