package com.ezbuyshop.product.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductModel {
	
	private String productId;
	
	private String title;

	private BigDecimal price;
	
	private Integer quantity;

}
