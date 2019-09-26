package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderRspDto;
import com.xteamstudio.exam.oms.lottery.dto.RevokeOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.UserOrderDto;
import java.time.ZonedDateTime;
import reactor.core.publisher.Mono;

public interface LotteryService {

    Mono<PlaceOrderRspDto> placeOrder(String name, PlaceOrderDto placeOderDto);

    Mono<RevokeOrderDto> revokeOrder(String name, String id);

    Mono<UserOrderDto> getOrder(String name, ZonedDateTime start, ZonedDateTime end);
}
