package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.entity.UserBalanceEntity;
import com.xteamstudio.exam.oms.lottery.dto.UserInfoDto;
import com.xteamstudio.exam.oms.lottery.repository.UserBalanceRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UserServiceImpl implements UserService {

    private UserBalanceRepository userBalanceRepository;

    @Autowired
    public void setUserBalanceRepository(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    /**
     * decrease money, and create new balance entity for users.
     * it occur retry when update failed, because optimistic lock.
     *
     * @param oldBalance old balance
     * @param money      decreased money
     * @return new balance record
     */
    @Retryable(include = OptimisticEntityLockException.class)
    public UserBalanceEntity minusMoney(UserBalanceEntity oldBalance, BigDecimal money) {
        UserBalanceEntity newBalance = createNewBalance(oldBalance, money);
        newBalance = userBalanceRepository.save(newBalance);
        return newBalance;
    }

    /**
     * get user info.
     *
     * @param name user name
     * @return user info dto
     */
    @Override
    public Mono<UserInfoDto> getInfo(String name) {
        Optional<UserBalanceEntity> byId = userBalanceRepository.findById(name);
        if (byId.isPresent()) {
            UserBalanceEntity userBalanceEntity = byId.get();
            UserInfoDto dto = new UserInfoDto();
            dto.setUid(userBalanceEntity.getUid());
            dto.setBalance(userBalanceEntity.getMoney());
            return Mono.just(dto);
        } else {
            throw new LotteryException("no balance", LotteryException.error_1);
        }
    }

    /**
     * add money, and create new balance entity for users.
     * it occur retry when update failed, because optimistic lock.
     *
     * @param oldBalance old balance
     * @param money      want increased money
     * @return increased money recorded as entity
     */
    @Retryable(include = OptimisticEntityLockException.class)
    public UserBalanceEntity addMoney(UserBalanceEntity oldBalance, BigDecimal money) {
        String newMoney = addMoneyWhenOrder(oldBalance, money);
        UserBalanceEntity n = new UserBalanceEntity();
        n.setVersion(oldBalance.getVersion());
        n.setMoney(newMoney);
        n.setUid(oldBalance.getUid());
        return userBalanceRepository.save(n);
    }

    private String addMoneyWhenOrder(UserBalanceEntity userBalanceEntity, BigDecimal money) {
        return new BigDecimal(userBalanceEntity.getMoney()).add(money).toPlainString();
    }

    private UserBalanceEntity createNewBalance(UserBalanceEntity oldBalance, BigDecimal money) {
        String newMoney = subtractMoneyWhenOrder(oldBalance, money);
        UserBalanceEntity n = new UserBalanceEntity();
        n.setVersion(oldBalance.getVersion());
        n.setMoney(newMoney);
        n.setUid(oldBalance.getUid());
        return n;
    }

    private String subtractMoneyWhenOrder(UserBalanceEntity userBalanceEntity, BigDecimal m) {
        return new BigDecimal(userBalanceEntity.getMoney()).subtract(m).toPlainString();
    }
}
