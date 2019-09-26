package com.xteamstudio.exam.oms.lottery.repository;

import com.xteamstudio.exam.oms.entity.UserConfigEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserConfigEntity, String> {
}
