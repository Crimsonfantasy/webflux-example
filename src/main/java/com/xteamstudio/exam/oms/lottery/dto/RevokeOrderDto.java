package com.xteamstudio.exam.oms.lottery.dto;

public class RevokeOrderDto {
    private String oldMoney;
    private String newMoney;
    private String money;
    private String orderId;

    public void setOldMoney(String oldMoney) {
        this.oldMoney = oldMoney;
    }

    public String getOldMoney() {
        return oldMoney;
    }

    public void setNewMoney(String newMoney) {
        this.newMoney = newMoney;
    }

    public String getNewMoney() {
        return newMoney;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
