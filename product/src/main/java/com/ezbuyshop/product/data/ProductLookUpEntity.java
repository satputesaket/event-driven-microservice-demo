package com.ezbuyshop.product.data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="productlookup")
public class ProductLookUpEntity implements Serializable{

	private static final long serialVersionUID = -2070308298001707785L;
	
	@Id
	private String productId;
	
	@Column(unique=true)
	private String title;

}
