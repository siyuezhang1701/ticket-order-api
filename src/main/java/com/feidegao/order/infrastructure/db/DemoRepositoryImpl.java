package com.feidegao.order.infrastructure.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.feidegao.order.entity.DemoEntity;
import com.feidegao.order.repository.DemoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public class DemoRepositoryImpl implements DemoRepository {
    private final DynamoDBMapper dbMapper;

    public DemoRepositoryImpl(DynamoDBMapper dbMapper) {
        this.dbMapper = dbMapper;
    }


    @Override
    public String saveDemoEntity() {
        String id = UUID.randomUUID().toString();
        DemoEntity demoEntity = DemoEntity.builder().id(id).build();
        dbMapper.save(demoEntity);
        return id;
    }
}
