package com.feidegao.order.infrastructure.serviceclient;

import com.feidegao.order.serviceclient.FlightClient;
import com.feidegao.order.infrastructure.serviceclient.mapper.FlightMapper;
import com.feidegao.order.response.FlightResponse;
import com.feidegao.order.model.Flight;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FlightClientImpl implements FlightClient {
    private final RestTemplate restTemplate;

    public FlightClientImpl(RestTemplateBuilder restTemplateBuilder) {
        restTemplate =restTemplateBuilder.build();
    }

    @Override
    public Flight getFlight(String flightNo) {
        FlightResponse flightResponse = restTemplate.getForObject("/flights/{fid}", FlightResponse.class, flightNo);
        return FlightMapper.MAPPER.mapToModel(flightResponse);
    }
}
