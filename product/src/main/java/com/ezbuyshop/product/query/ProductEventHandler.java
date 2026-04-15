package com.ezbuyshop.product.query;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ezbuyshop.product.command.event.ProductCreatedEvent;
import com.ezbuyshop.product.data.ProductEntity;
import com.ezbuyshop.product.data.ProductRepository;

@Component
public class ProductEventHandler {
	
	private final ProductRepository productRepository;
	
	public ProductEventHandler(ProductRepository productRepository) {
		this.productRepository=productRepository;
	}
	
	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {
		
		if(productRepository.existsById(productCreatedEvent.getProductId())) {
			return;
		}
		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
		productRepository.save(productEntity);
	}
	

}
