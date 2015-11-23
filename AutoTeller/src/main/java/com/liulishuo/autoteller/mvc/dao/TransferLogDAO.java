package com.liulishuo.autoteller.mvc.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
public interface TransferLogDAO {
    static final String table = "transfer_log";

    @Insert("INSERT INTO " + table + "(from_user, to_user, coins) " +
            "VALUES(#{fromUser}, #{toUser}, #{coins})")
    public void logTransaction(@Param("fromUser") BigInteger fromUser,
                               @Param("toUser") BigInteger toUser,
                               @Param("coins") int coins);

}
