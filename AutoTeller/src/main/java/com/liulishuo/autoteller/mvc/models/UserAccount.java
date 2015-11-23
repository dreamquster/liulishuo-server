package com.liulishuo.autoteller.mvc.models;

import java.math.BigInteger;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
public class UserAccount {

    private BigInteger userId;
    private int coins;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}
