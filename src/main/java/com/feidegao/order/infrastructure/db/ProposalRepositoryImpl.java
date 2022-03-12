package com.feidegao.order.infrastructure.db;

import com.feidegao.order.model.Proposal;
import com.feidegao.order.repository.ProposalRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProposalRepositoryImpl implements ProposalRepository {
    @Override
    public Proposal getByOriginTicketId(String originTicketId) {
        return null;
    }
}
