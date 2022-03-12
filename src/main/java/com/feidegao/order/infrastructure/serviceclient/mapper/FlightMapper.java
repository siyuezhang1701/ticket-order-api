package com.feidegao.order.infrastructure.serviceclient.mapper;

import com.feidegao.order.infrastructure.serviceclient.response.FlightResponse;
import com.feidegao.order.model.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FlightMapper {
    FlightMapper MAPPER = Mappers.getMapper(FlightMapper.class);


    @Mapping(
            target = "status",
            expression = "java(com.feidegao.order.model.FlightStatus.valueOf(response.getStatus()))"
    )
    Flight mapToModel(FlightResponse response);
}
