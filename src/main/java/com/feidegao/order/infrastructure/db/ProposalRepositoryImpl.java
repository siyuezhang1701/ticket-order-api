package com.feidegao.order.infrastructure.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.feidegao.order.entity.ProposalEntity;
import com.feidegao.order.infrastructure.db.mapper.ProposalMapper;
import com.feidegao.order.model.Proposal;
import com.feidegao.order.repository.ProposalRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

import static com.feidegao.order.entity.ProposalEntity.ORIGIN_TICKET_ID_INDEX;

@Repository
public class ProposalRepositoryImpl implements ProposalRepository {
    private final DynamoDBMapper mapper;

    public ProposalRepositoryImpl(DynamoDBMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Proposal getByOriginTicketId(String originTicketId) {
        DynamoDBQueryExpression<ProposalEntity> query = new DynamoDBQueryExpression<>();
        query
                .withIndexName(ORIGIN_TICKET_ID_INDEX)
                .withConsistentRead(false)
                .withHashKeyValues(ProposalEntity.builder().originTicketId(originTicketId).build());
        ArrayList<ProposalEntity> res = new ArrayList<>(mapper.query(ProposalEntity.class, query));
        return ProposalMapper.MAPPER.mapToModel(res.get(0));
    }
}
