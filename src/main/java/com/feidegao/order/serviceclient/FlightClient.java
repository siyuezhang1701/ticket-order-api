package com.feidegao.order.serviceclient;

import com.feidegao.order.model.Flight;

public interface FlightClient {
    Flight getFlight(String flightNo);
}
