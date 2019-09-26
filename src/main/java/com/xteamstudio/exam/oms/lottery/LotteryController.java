package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderRspDto;
import com.xteamstudio.exam.oms.lottery.dto.RevokeOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.UserInfoDto;
import com.xteamstudio.exam.oms.lottery.dto.UserOrderDto;
import java.security.Principal;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "x_team")
public class LotteryController {

    private static final Logger logger = LoggerFactory.getLogger(LotteryController.class);

    private LotteryService lotteryService;

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public LotteryController(LotteryService lotteryService) {
        this.lotteryService = lotteryService;
    }

    /**
     * order a lottery.
     *
     * @param placeOderDto request body
     * @param principal client principle
     * @return dto for client
     */
    @PutMapping("/lottery")
    @PreAuthorize("hasRole('USER')")
    public Mono<PlaceOrderRspDto> placeOrder(@Validated @RequestBody PlaceOrderDto placeOderDto,
                                             Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .flatMap(name -> lotteryService.placeOrder(name, placeOderDto))
                .doOnError(this::onError);
    }

    /**
     * revoke a lottery.
     *
     * @param id order id
     * @param principal security principal
     * @return dto for client
     */
    @DeleteMapping("/lottery/id/{id}")
    @PreAuthorize("hasRole('USER')")
    public Mono<RevokeOrderDto> revokeOrder(@Validated @PathVariable String id,
                                            Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .flatMap(name -> lotteryService.revokeOrder(name, id))
                .doOnError(this::onError);
    }

    /**
     * find user balance.
     *
     * @param principal client user principal
     * @return dto for client
     */
    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public Mono<UserInfoDto> getUserBalance(Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .flatMap(name -> userService.getInfo(name))
                .doOnError(this::onError);
    }

    /**
     * find order wit given time.
     *
     * @param start start time
     * @param end end time
     * @param principal client principal
     * @return dto for client
     */
    @GetMapping("/order/start/{start}/end/{end}")
    @PreAuthorize("hasRole('USER')")
    public Mono<UserOrderDto> getOrder(@PathVariable
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                           ZonedDateTime start,
                                       @PathVariable
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                           ZonedDateTime end,
                                       Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .flatMap(name -> lotteryService.getOrder(name, start, end))
                .doOnError(this::onError);
    }

    private void onError(Throwable throwable) {
        logger.error("failed", throwable);
        if (throwable instanceof LotteryException) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, throwable.getMessage());
        }
        throw new RuntimeException(throwable);
    }
}