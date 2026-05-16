package com.ezbuyshop.product.query;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindProductQuery implements Serializable {

	private static final long serialVersionUID = -6470270646135796146L;
    private String filter;


}