package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.entity.SixNumberEntity;
import com.xteamstudio.exam.oms.entity.SixNumberOrderDetailEntity;
import com.xteamstudio.exam.oms.entity.UserBalanceEntity;
import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderRspDto;
import com.xteamstudio.exam.oms.lottery.dto.RevokeOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.UserOrderDto;
import com.xteamstudio.exam.oms.lottery.repository.SixNumberOrderRepository;
import com.xteamstudio.exam.oms.lottery.repository.SixOrderDetailRepository;
import com.xteamstudio.exam.oms.lottery.repository.UserBalanceRepository;
import com.xteamstudio.exam.oms.lottery.repository.UserRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
import reactor.core.publisher.Mono;

@Service
public class LotteryServiceImpl implements LotteryService {

    private static BigDecimal LOTTERY_MONEY = new BigDecimal("50");

    private UserRepository userRepository;

    private TransactionTemplate transactionTemplate;

    private UserBalanceRepository userBalanceRepository;

    private SixNumberOrderRepository sixNumberOrderRepository;

    private SixOrderDetailRepository sixOrderDetailRepository;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setSixOrderDetailRepository(SixOrderDetailRepository sixOrderDetailRepository) {
        this.sixOrderDetailRepository = sixOrderDetailRepository;
    }

    @Autowired
    public void setSixNumberOrderRepository(SixNumberOrderRepository sixNumberOrderRepository) {
        this.sixNumberOrderRepository = sixNumberOrderRepository;
    }

    @Autowired
    public void setUserBalanceRepository(UserBalanceRepository userBalanceRepository) {
        this.userBalanceRepository = userBalanceRepository;
    }

    @Autowired
    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        this.transactionTemplate = transactionTemplate;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<UserOrderDto> getOrder(String name, ZonedDateTime start, ZonedDateTime end) {
        return Mono.fromCallable(() -> handleFindOrder(name, start, end));
    }

    private UserOrderDto toUserOrderDto(List<SixNumberEntity> e) {
        List<UserOrderDto.Order> collect = e.stream()
                .map(this::toUserOrder).collect(Collectors.toList());
        UserOrderDto d = new UserOrderDto();
        d.setOrders(collect);
        return d;
    }

    private UserOrderDto.Order toUserOrder(SixNumberEntity sixNumberEntity) {
        UserOrderDto.Order o = new UserOrderDto.Order();
        o.setId(sixNumberEntity.getOrderId());
        o.setNumbers(sixNumberEntity.getDetails().stream()
                .map(SixNumberOrderDetailEntity::getNumber).collect(Collectors.toSet()));
        return o;
    }

    private UserOrderDto handleFindOrder(String name, ZonedDateTime start, ZonedDateTime end) {
        return transactionTemplate.execute(status -> {
            List<SixNumberEntity> s = sixNumberOrderRepository.findByUidEqualsAndCreatedTimeBetween(
                    name, start.toLocalDateTime(), end.toLocalDateTime());
            return toUserOrderDto(s);
        });
    }

    @Override
    public Mono<PlaceOrderRspDto> placeOrder(String name, PlaceOrderDto placeOderDto) {
        return Mono.fromCallable(() -> handlePlaceOder(name, placeOderDto))
                .map(this::toPlaceOrderDto);
    }

    @Override
    public Mono<RevokeOrderDto> revokeOrder(String name, String id) {
        return Mono.fromCallable(() -> handleRevokeOrder(name, id));
    }

    private RevokeOrderDto handleRevokeOrder(String name, String id) {
        return transactionTemplate.execute(
            o -> startRevokeOrderTransaction(o, name, id));
    }

    private RevokeOrderDto startRevokeOrderTransaction(TransactionStatus o,
                                                       String name,
                                                       String id) {
        try {
            RevokeOrderDto dto = new RevokeOrderDto();
            Optional<SixNumberEntity> present = sixNumberOrderRepository.findById(id);
            if (!present.isPresent()) {
                throw new LotteryException("order " + id + " no found", LotteryException.error_2);
            }
            SixNumberEntity order = present.get();
            if (!name.equals(order.getUid())) {
                throw new LotteryException(
                        "order " + id + " can be deleted by that is not a owner",
                        LotteryException.error_3);
            }
            BigDecimal money = order.getMoney();
            Optional<UserBalanceEntity> balance = userBalanceRepository.findById(name);
            if (!balance.isPresent()) {
                throw new LotteryException("no money exists", LotteryException.error_1);
            } else {
                UserBalanceEntity oldBalance = balance.get();
                UserBalanceEntity newBalance = userService.addMoney(oldBalance, money);
                String orderId = order.getOrderId();
                sixNumberOrderRepository.deleteById(orderId);
                dto.setOldMoney(oldBalance.getMoney());
                dto.setNewMoney(newBalance.getMoney());
                dto.setMoney(money.toPlainString());
                dto.setOrderId(id);
                return dto;
            }
        } catch (Throwable e) {
            o.setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    private PlaceOrderRspDto toPlaceOrderDto(PlaceOrderContext o) {
        PlaceOrderRspDto dto = new PlaceOrderRspDto();
        UserBalanceEntity balance = o.getBalance();
        dto.setMoney(balance.getMoney());
        dto.setUid(balance.getUid());
        SixNumberEntity order = o.getOrder();
        PlaceOrderRspDto.Order orderDto = new PlaceOrderRspDto.Order();
        orderDto.setId(order.getOrderId());
        Set<Integer> numbers = order.getDetails().stream().map(
                SixNumberOrderDetailEntity::getNumber).collect(Collectors.toSet());
        orderDto.setNumbers(numbers);
        dto.setOrder(orderDto);
        return dto;
    }


    static class PlaceOrderContext {

        private UserBalanceEntity balance;
        private UserBalanceEntity newBalance;
        private SixNumberEntity order;

        void setBalance(UserBalanceEntity balance) {
            this.balance = balance;
        }

        UserBalanceEntity getBalance() {
            return balance;
        }

        void setNewBalance(UserBalanceEntity newBalance) {
            this.newBalance = newBalance;
        }

        public UserBalanceEntity getNewBalance() {
            return newBalance;
        }

        void setOrder(SixNumberEntity order) {
            this.order = order;
        }

        SixNumberEntity getOrder() {
            return order;
        }
    }

    private PlaceOrderContext handlePlaceOder(String name, PlaceOrderDto placeOderDto) {
        return transactionTemplate.execute(o -> startPlaceOrderTransaction(o, name, placeOderDto));
    }

    private PlaceOrderContext startPlaceOrderTransaction(TransactionStatus transactionStatus,
                                                         String name, PlaceOrderDto placeOderDto) {

        try {
            Optional<UserBalanceEntity> old = userBalanceRepository.findById(name);
            if (!old.isPresent()) {
                throw new LotteryException("no money exists", LotteryException.error_1);
            }
            final UserBalanceEntity oldBalance = old.get();
            final UserBalanceEntity newBalance = userService.minusMoney(oldBalance, LOTTERY_MONEY);
            PlaceOrderContext p = new PlaceOrderContext();
            p.setBalance(oldBalance);
            SixNumberEntity savedOrder = savePrimaryOrder(name);
            List<Integer> numbers = placeOderDto.getNumbers();
            Set<SixNumberOrderDetailEntity> details = numbers.stream().map(
                    this::toOderDetail).collect(Collectors.toSet());
            p.setOrder(savedOrder);
            sixOrderDetailRepository.saveAll(details);
            savedOrder.getDetails().addAll(details);
            p.setNewBalance(newBalance);
            return p;
        } catch (Throwable e) {
            transactionStatus.setRollbackOnly();
            throw new RuntimeException(e);
        }
    }

    private SixNumberEntity savePrimaryOrder(String name) {
        SixNumberEntity oder = new SixNumberEntity();
        oder.setUid(name);
        oder.setOrderId(UUID.randomUUID().toString());
        oder.setMoney(LOTTERY_MONEY);
        oder.setCreatedTime(LocalDateTime.now());
        return sixNumberOrderRepository.save(oder);
    }

    private SixNumberOrderDetailEntity toOderDetail(Integer n) {
        SixNumberOrderDetailEntity s = new SixNumberOrderDetailEntity();
        s.setId(UUID.randomUUID().toString());
        s.setNumber(n);
        return s;
    }
}