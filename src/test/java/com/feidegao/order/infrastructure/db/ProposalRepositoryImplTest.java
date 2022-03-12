package com.feidegao.order.infrastructure.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.feidegao.order.entity.ProposalEntity;
import com.feidegao.order.infrastructure.BaseIntegrationTest;
import com.feidegao.order.model.Proposal;
import com.feidegao.order.repository.ProposalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProposalRepositoryImplTest extends BaseIntegrationTest {
    @Autowired
    private DynamoDBMapper dynamoDBMapper;

    @Autowired
    private ProposalRepository proposalRepository;

    @BeforeEach
    void setUp() {
        createTable(ProposalEntity.class);
    }

    @Test
    void should_find_order_by_origin_ticket_id() {
        dynamoDBMapper.save(ProposalEntity.builder().id("2").originTicketId("1").build());

        Proposal proposal = Proposal.builder().id("2").originTicketId("1").build();

        assertEquals(proposal, proposalRepository.getByOriginTicketId("1"));
    }
}