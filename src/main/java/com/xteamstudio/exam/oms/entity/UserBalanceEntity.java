package com.xteamstudio.exam.oms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "USER_BALANCE")
public class UserBalanceEntity {

    @Id
    @Column(name = "UID")
    private String uid;

    @Column(name = "MONEY")
    private String money;

    @Version
    @Column(name = "VERSION")
    private long version;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}

