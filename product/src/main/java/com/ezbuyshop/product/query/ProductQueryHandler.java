package com.ezbuyshop.product.query;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ezbuyshop.product.data.ProductEntity;
import com.ezbuyshop.product.data.ProductRepository;
import com.ezbuyshop.product.dto.ProductModel;


@Component
public class ProductQueryHandler {
	
	private final ProductRepository productRepository;
	
	@Autowired
	public ProductQueryHandler(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@QueryHandler
	public List<ProductModel> findProducts(FindProductQuery findProductQuery) {
		

		    List<ProductModel> productsRest = new ArrayList<>();

		    List<ProductEntity> storedProducts = productRepository.findAll();

		    for (ProductEntity productEntity : storedProducts) {
		    	ProductModel productRestModel = new ProductModel();
		        BeanUtils.copyProperties(productEntity, productRestModel);
		        productsRest.add(productRestModel);
		    }

		    return productsRest;
		}
	
}
