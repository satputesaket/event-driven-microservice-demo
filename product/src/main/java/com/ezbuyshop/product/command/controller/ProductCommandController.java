package com.ezbuyshop.product.command.controller;

import java.util.UUID;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezbuyshop.product.command.CreateProductCommand;
import com.ezbuyshop.product.dto.CreateProductModel;

@RestController
//@RequestMapping("/api/v1")
@RequestMapping("/products")
public class ProductCommandController {
	
	@Autowired
	Environment env;
	
	@Autowired
	CommandGateway commandGateway;
	
	
	
//	@GetMapping("/products")
//	public String getProducts() {
//		return "get Method called"+ env.getProperty("local.server.port");
//	}
	
	
	@PostMapping()
	public String createProducts(@RequestBody CreateProductModel createProductModel) {
		

		
		CreateProductCommand createProductCommand =CreateProductCommand.builder()
		.title(createProductModel.getTitle())
		.price(createProductModel.getPrice())
		.quantity(createProductModel.getQuantity())
		.productId(UUID.randomUUID().toString()).build();
		String returnString;
		try{
			returnString = commandGateway.sendAndWait(createProductCommand);
		}catch(Exception ex) {
			returnString = ex.getLocalizedMessage();
		}
		return returnString;
	}
	
//	@PutMapping("/products")
//	public String putProducts() {
//		return "put Method called";
//	}
//	
//	
//	@DeleteMapping("/products")
//	public String deleteProducts() {
//		return "delete Method called";
//	}

}
