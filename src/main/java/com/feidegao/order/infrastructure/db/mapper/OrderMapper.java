package com.feidegao.order.infrastructure.db.mapper;

import com.feidegao.order.entity.OrderEntity;
import com.feidegao.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    Order mapToModel(OrderEntity orderEntity);

    OrderEntity mapToEntity(Order order);
}
