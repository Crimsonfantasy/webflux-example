package com.xteamstudio.exam.oms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "SIX_NUMBER_ORDER")
public class SixNumberEntity {

    @Id
    @Column(name = "ORDER_ID")
    private String orderId;

    @Column(name = "UID")
    private String uid;

    @Column(name = "MONEY")
    private BigDecimal money;

    @Column(name = "CREATE_TIME")
    private LocalDateTime createdTime;

    @OneToMany(orphanRemoval = true
    )
    @JoinColumn(name = "ORDER_ID")
    private Set<SixNumberOrderDetailEntity> details = new HashSet<>();

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public Set<SixNumberOrderDetailEntity> getDetails() {
        return details;
    }

    public void setDetails(Set<SixNumberOrderDetailEntity> details) {
        this.details = details;
    }
}
