package com.feidegao.order.infrastructure.db.mapper;

import com.feidegao.order.infrastructure.db.entity.ProposalEntity;
import com.feidegao.order.model.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProposalMapper {

    ProposalMapper MAPPER = Mappers.getMapper(ProposalMapper.class);

    Proposal mapToModel(ProposalEntity entity);
}
