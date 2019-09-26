package com.xteamstudio.exam.oms.lottery.dto;

import java.util.Set;

public class PlaceOrderRspDto {

    private String money;

    private String uid;

    private PlaceOrderRspDto.Order order;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public static class Order {

        private String id;

        private Set<Integer> numbers;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Set<Integer> getNumbers() {
            return numbers;
        }

        public void setNumbers(Set<Integer> numbers) {
            this.numbers = numbers;
        }
    }
}
