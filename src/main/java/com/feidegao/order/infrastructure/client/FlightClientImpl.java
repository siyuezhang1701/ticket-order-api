package com.feidegao.order.infrastructure.client;

import com.feidegao.order.client.FlightClient;
import com.feidegao.order.infrastructure.client.mapper.FlightMapper;
import com.feidegao.order.infrastructure.client.response.FlightResponse;
import com.feidegao.order.model.Flight;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
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
