package com.liulishuo.autoteller.mvc.dao;

import com.liulishuo.autoteller.mvc.models.UserAccount;
import org.apache.ibatis.annotations.*;
import org.mybatis.spring.annotation.MapperScan;

import java.math.BigInteger;

/**
 * Created by Administrator on 2015/11/19 0019.
 */
public interface CoinsDAO {

    static final String table = "user_coins";

    @Insert("INSERT INTO " + table + "(user_id, coins) VALUES (#{userId}, #{addCoins})")
    public int createUser(@Param("userId") BigInteger userId, @Param("addCoins") int addCoins);

    @Results({
            @Result(property = "userId", column = "user_id")
    })
    @Select("SELECT user_id,coins FROM " + table + " WHERE user_id=#{userId}")
    public UserAccount getUserAccount(@Param("userId") BigInteger userId);

    // 修改用户的coins数据，deltaCoins为负值代表从该userId转出coins.
    @Update("UPDATE " + table + " SET coins=coins+#{deltaCoins} WHERE user_id=#{userId}")
    public int depositCoin(@Param("userId") BigInteger userId, @Param("deltaCoins") int deltaCoins);
}
