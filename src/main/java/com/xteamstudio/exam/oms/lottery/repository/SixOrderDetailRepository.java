package com.xteamstudio.exam.oms.lottery.repository;

import com.xteamstudio.exam.oms.entity.SixNumberOrderDetailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SixOrderDetailRepository
        extends CrudRepository<SixNumberOrderDetailEntity, String> {
}
