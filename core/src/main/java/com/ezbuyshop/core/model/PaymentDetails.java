package com.ezbuyshop.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PaymentDetails {
    private  String name;
    private  String cardNumber;
    private  int validUntilMonth;
    private  int validUntilYear;
    private  String cvv;
    
}