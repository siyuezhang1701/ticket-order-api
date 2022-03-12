package com.feidegao.order.client;

import com.feidegao.order.model.Flight;

public interface FlightClient {
    Flight getFlight(String flightNo);
}
