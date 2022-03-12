package com.feidegao.order.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feidegao.order.infrastructure.client.FlightClientImpl;
import com.feidegao.order.infrastructure.client.response.FlightResponse;
import com.feidegao.order.model.Flight;
import com.feidegao.order.model.FlightStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@RestClientTest(FlightClientImpl.class)
public class FlightClientTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private FlightClientImpl flightClint;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void should_return_flight_info_when_flight_is_in_flight() throws JsonProcessingException {
        this.server.expect(requestTo("/flights/1"))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(FlightResponse.builder().status("IN_FLIGHT").build()), MediaType.APPLICATION_JSON));

        Flight flight = flightClint.getFlight("1");

        assertEquals(Flight.builder().status(FlightStatus.IN_FLIGHT).build(), flight);
    }

    @Test
    public void should_return_flight_info_when_flight_is_ready() throws JsonProcessingException {
        this.server.expect(requestTo("/flights/1"))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(FlightResponse.builder().status("READY").build()), MediaType.APPLICATION_JSON));

        Flight flight = flightClint.getFlight("1");

        assertEquals(Flight.builder().status(FlightStatus.READY).build(), flight);
    }
}
