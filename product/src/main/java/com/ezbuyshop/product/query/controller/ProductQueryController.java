package com.ezbuyshop.product.query.controller;

import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezbuyshop.product.dto.ProductModel;
import com.ezbuyshop.product.query.FindProductQuery;

@RestController
@RequestMapping("/product")
public class ProductQueryController {

	@Autowired
	QueryGateway queryGateway;

	@GetMapping
	public List<ProductModel> getProducts() {
		FindProductQuery findProductQuery = new FindProductQuery();
		List<ProductModel> products = queryGateway
				.query(findProductQuery, ResponseTypes.multipleInstancesOf(ProductModel.class)).join();
		return products;
	}

}
