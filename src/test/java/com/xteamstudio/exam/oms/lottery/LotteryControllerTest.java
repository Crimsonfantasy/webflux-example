package com.xteamstudio.exam.oms.lottery;

import com.xteamstudio.exam.oms.OrderManagementSystemApplication;
import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.PlaceOrderRspDto;
import com.xteamstudio.exam.oms.lottery.dto.RevokeOrderDto;
import com.xteamstudio.exam.oms.lottery.dto.UserInfoDto;
import com.xteamstudio.exam.oms.lottery.dto.UserOrderDto;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OrderManagementSystemApplication.class)
@FixMethodOrder()
public class LotteryControllerTest {

    private String username = "fenrir";
    private String password = "fenrir";
    private String base64encodedUsernameAndPassword;

    @Before
    public void before() {
        this.base64encodedUsernameAndPassword = base64Encode(username + ":" + password);
    }

    @Test
    public void placeOrder() {
        final PlaceOrderDto dto = new PlaceOrderDto();
        List<Integer> l = new ArrayList<>();
        l.add(3);
        l.add(5);
        l.add(9);
        l.add(10);
        dto.setNumbers(l);
        Mono<PlaceOrderRspDto> rsp = WebClient.create().put()
                .uri("http://localhost:8083/x_team/lottery")
                .body(Mono.just(dto), PlaceOrderDto.class)
                .retrieve()
                .bodyToMono(PlaceOrderRspDto.class);
        try {
            PlaceOrderRspDto block = rsp.block(Duration.ofSeconds(3));
        } catch (WebClientResponseException w) {
            HttpStatus statusCode = w.getStatusCode();
            Assert.assertEquals(HttpStatus.UNAUTHORIZED, statusCode);
        }
    }

    private static String base64Encode(String stringToEncode) {
        return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
    }

    @Test
    public void placeOrder2() {
        PlaceOrderRspDto block = doPlaceOrderRspDto();
        assert block != null;
        String money = block.getMoney();
        //假設一張彩卷50元
        Assert.assertEquals("9950.0", money);
    }

    private PlaceOrderRspDto doPlaceOrderRspDto() {
        final PlaceOrderDto dto = new PlaceOrderDto();
        List<Integer> l = new ArrayList<>();
        l.add(3);
        l.add(5);
        l.add(9);
        l.add(10);
        dto.setNumbers(l);
        Mono<PlaceOrderRspDto> rsp = WebClient.create().put()
                .uri("http://localhost:8083/x_team/lottery")
                .body(Mono.just(dto), PlaceOrderDto.class)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64encodedUsernameAndPassword)
                .retrieve()
                .bodyToMono(PlaceOrderRspDto.class);
        return rsp.block(Duration.ofSeconds(3));
    }

    @Test
    public void revokeOrder() {
        PlaceOrderRspDto dto = doPlaceOrderRspDto();
        Mono<RevokeOrderDto> rsp = WebClient.create().delete()
                .uri("http://localhost:8083/x_team/lottery/id/{id}", dto.getOrder().getId())
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64encodedUsernameAndPassword)
                .retrieve()
                .bodyToMono(RevokeOrderDto.class);
        RevokeOrderDto block = rsp.block(Duration.ofSeconds(3));
        assert block != null;
        String money = block.getNewMoney();
        Assert.assertEquals("10000.0", money);
    }

    @Test
    public void getUserBalance() {
        Mono<UserInfoDto> rsp = WebClient.create().get()
                .uri("http://localhost:8083/x_team/balance")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64encodedUsernameAndPassword)
                .retrieve()
                .bodyToMono(UserInfoDto.class);
        UserInfoDto block = rsp.block(Duration.ofSeconds(3));
        assert block != null;
        String balance = block.getBalance();
        Assert.assertEquals("10000.0", balance);
    }

    @Test
    public void getOrder() {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime yesterday = now.plusDays(-1);
        Mono<UserOrderDto> rsp = WebClient.create().get()
                .uri("http://localhost:8083/x_team/order/start/{start}/end/{end}", yesterday.format(DateTimeFormatter.ISO_DATE_TIME), now.format(DateTimeFormatter.ISO_DATE_TIME))
                .header(HttpHeaders.AUTHORIZATION, "Basic " + base64encodedUsernameAndPassword)
                .retrieve()
                .bodyToMono(UserOrderDto.class);
        UserOrderDto u = rsp.block(Duration.ofSeconds(3));
        assert u != null;
        List<UserOrderDto.Order> orders = u.getOrders();
        Assert.assertEquals(1, orders.size());
    }
}