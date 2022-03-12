package com.feidegao.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    private String id;
    private String flightNo;
    private InvoiceRequest invoiceRequest;
    private float amount;
    private float insuranceAmount;
}
