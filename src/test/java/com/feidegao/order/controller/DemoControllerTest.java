package com.feidegao.order.controller;

import com.feidegao.order.service.DemoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DemoControllerTest extends BaseControllerIntegrationTest {

    @MockBean
    private DemoService demoService;

    @Test
    void test_heart_beat() throws Exception {
        performGet(
                "/heartbeat"
        )
                .andExpect(status().isOk());
    }
}
