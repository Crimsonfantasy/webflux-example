package com.xteamstudio.exam.oms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SIX_NUMBER_ORDER_DETAIL")
public class SixNumberOrderDetailEntity {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "NUMBER")
    private Integer number;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
