package com.ezbuyshop.product.dto;


import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRestModel implements Serializable {

	private static final long serialVersionUID = 6380494498860474474L;
	private List<ProductModel> products;
}