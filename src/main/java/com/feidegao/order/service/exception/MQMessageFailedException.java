package com.feidegao.order.service.exception;

public class MQMessageFailedException extends RuntimeException{

    public MQMessageFailedException(String message) {
        super(message);
    }
}
