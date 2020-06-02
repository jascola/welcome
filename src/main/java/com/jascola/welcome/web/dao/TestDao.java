package com.jascola.welcome.web.dao;

import com.jascola.welcome.web.entity.TestEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Mapper
public interface TestDao {

    List<TestEntity> getTestEntityAll();

    List<TestEntity> getTestEntityRow(RowBounds rowBounds);

    List<TestEntity> getTestEntity(TestEntity entity);

    int insert(TestEntity testEntity);

}
