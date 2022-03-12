package com.feidegao.order.repository;

import com.feidegao.order.model.Proposal;

public interface ProposalRepository {
    Proposal getByOriginTicketId(String originTicketId);
}
