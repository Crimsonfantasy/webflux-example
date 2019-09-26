package com.xteamstudio.exam.oms.lottery.dto;

import java.util.List;
import java.util.Set;

public class UserOrderDto {

    private List<Order> orders;


    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
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
