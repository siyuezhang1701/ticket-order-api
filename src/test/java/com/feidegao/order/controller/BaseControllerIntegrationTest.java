package com.feidegao.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@TestPropertySource(
        properties = {
                "server.port = 9876",
        }
)
@ActiveProfiles("test")
@WebMvcTest
public class BaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    ResultActions performPost(String path, Object requestBody)
            throws Exception {
        return mockMvc.perform(
                post(path)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(requestBody))
        );
    }

    ResultActions performGet(String path) throws Exception {
        return mockMvc.perform(get(path));
    }
}
