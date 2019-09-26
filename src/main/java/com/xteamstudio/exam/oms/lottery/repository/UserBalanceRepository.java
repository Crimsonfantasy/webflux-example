package com.xteamstudio.exam.oms.lottery.repository;

import com.xteamstudio.exam.oms.entity.UserBalanceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBalanceRepository extends CrudRepository<UserBalanceEntity, String> {

}
