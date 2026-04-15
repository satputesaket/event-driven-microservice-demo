package com.ezbuyshop.product.data;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="products")
@Data
public class ProductEntity implements Serializable{

	private static final long serialVersionUID = -4425879436938879241L;
	
	@Id
	@Column(unique=true)
	private String productId;
	
	private String title;

	private BigDecimal price;
	
	private Integer quantity;
	
	

}
