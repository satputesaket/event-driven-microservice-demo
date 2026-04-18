package com.ezbuyshop.product.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.ezbuyshop.product.command.event.ProductCreatedEvent;
import com.ezbuyshop.product.data.ProductEntity;
import com.ezbuyshop.product.data.ProductRepository;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {
	
	private final ProductRepository productRepository;
	
	public ProductEventHandler(ProductRepository productRepository) {
		this.productRepository=productRepository;
	}
	
	@EventHandler
	public void on(ProductCreatedEvent productCreatedEvent) {


		ProductEntity productEntity = new ProductEntity();
		BeanUtils.copyProperties(productCreatedEvent, productEntity);
//		
//		try {
//			productRepository.save(productEntity);
//		} catch (IllegalArgumentException ex) {
//			ex.printStackTrace();
//		}
		
		try {
		    productRepository.save(productEntity);
		} catch (Exception ex) {
		    System.out.println("Duplicate or DB issue - ignoring");
		}

	}
	

}
