package com.xteamstudio.exam.oms.lottery.repository;

import com.xteamstudio.exam.oms.entity.SixNumberEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SixNumberOrderRepository extends CrudRepository<SixNumberEntity, String> {

    List<SixNumberEntity> findByUidEqualsAndCreatedTimeBetween(String name,
                                                               LocalDateTime start,
                                                               LocalDateTime end);
}
