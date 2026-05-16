package com.ezbuyshop.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel implements Serializable {

	
	private static final long serialVersionUID = -229868832382999854L;

	private String productId;
	
	private String title;

	private BigDecimal price;
	
	private Integer quantity;

}
