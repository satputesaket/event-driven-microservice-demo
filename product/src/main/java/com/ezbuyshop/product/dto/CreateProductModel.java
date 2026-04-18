package com.ezbuyshop.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProductModel {
	
	@NotBlank
	private String title;
	
	@Min(value = 1,message="Price cannot be zero or less than zero.")
	private BigDecimal price;
	
	@Min(value = 1,message="Quantity cannot be zero or less than zero.")
	private Integer quantity;

}
