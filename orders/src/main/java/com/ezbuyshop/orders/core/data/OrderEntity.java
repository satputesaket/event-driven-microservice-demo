package com.ezbuyshop.orders.core.data;

import java.io.Serializable;

import com.ezbuyshop.orders.core.model.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {


    private static final long serialVersionUID = -4161074312174547118L;
	
    @Id
    @Column(unique = true)
    public String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}