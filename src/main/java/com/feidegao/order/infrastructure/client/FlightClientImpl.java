package com.feidegao.order.infrastructure.client;

import com.feidegao.order.client.FlightClint;
import com.feidegao.order.model.Flight;
import org.springframework.stereotype.Component;

@Component
public class FlightClientImpl implements FlightClint {
    @Override
    public Flight getFlight(String flightNo) {
        return null;
    }
}
