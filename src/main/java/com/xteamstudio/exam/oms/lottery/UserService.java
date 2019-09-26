package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.entity.UserBalanceEntity;
import com.xteamstudio.exam.oms.lottery.dto.UserInfoDto;
import java.math.BigDecimal;
import reactor.core.publisher.Mono;

public interface UserService {

    /**
     * add money, and create new balance entity for users.
     *
     * @param oldBalance old balance
     * @param money      want increased money
     * @return increased money recorded as entity
     */
    UserBalanceEntity addMoney(UserBalanceEntity oldBalance, BigDecimal money);

    /**
     * decrease money, and create new balance entity for users.
     *
     * @param oldBalance old balance
     * @param money      want decreased money
     * @return decreased money recorded as entity
     */
    UserBalanceEntity minusMoney(UserBalanceEntity oldBalance, BigDecimal money);

    /**
     * get user info.
     *
     * @param name user name
     * @return user info dto
     */
    Mono<UserInfoDto> getInfo(String name);
}
