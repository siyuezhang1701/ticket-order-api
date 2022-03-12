package com.feidegao.order.service.exception;

public class InvalidInvoiceRequestException extends RuntimeException{

    public InvalidInvoiceRequestException(String message) {
        super(message);
    }
}
