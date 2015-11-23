package com.liulishuo.autoteller.mvc.controllers;

import com.liulishuo.autoteller.mvc.models.ApiResult;
import com.liulishuo.autoteller.mvc.services.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

/**
 * Created by Administrator on 2015/11/19 0019.
 */
@Controller
public class AutoTellerController {

    @Autowired
    private CoinService coinService;

    @RequestMapping(method = RequestMethod.POST, value = "/user/add")
    public @ResponseBody ApiResult depositCoins(
                       @RequestParam("user_id") BigInteger userId,
                       @RequestParam("coins") int coins) {
        ApiResult result = ApiResult.ILLEGAL_ARGS;
        if ((userId.compareTo(BigInteger.ZERO) <= 0) || coins <= 0) {
            result.appendMessage("user_id and coins both should be greater" +
                    " than 0, receive " + userId + " " + String.valueOf(coins));
            return result;
        }


        coinService.depositCoin(userId, coins);
        result = ApiResult.SUCCESS;
        result.appendMessage(String.format("add %d coins for %d", coins, userId));
        return result;
    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/transaction/transfer")
    public @ResponseBody ApiResult transferCoins(
                @RequestParam("from_user_id") BigInteger fromUser,
                @RequestParam("to_user_id") BigInteger toUser,
                @RequestParam("coins") int coins) {
        ApiResult result = ApiResult.ILLEGAL_ARGS;
        if ((fromUser.compareTo(BigInteger.ZERO) <= 0)
              || (toUser.compareTo(BigInteger.ZERO) <= 0)
              || coins <= 0) {
            result.appendMessage("from _user_id,to_user_id and coins " +
                    "both should be greater than 0, receive "
                    + fromUser + " " + toUser + " " + String.valueOf(coins));
        }


        try {
            coinService.transferCoins(fromUser, toUser, coins);
            result = ApiResult.SUCCESS;
            result.appendMessage(String.format("transfer %d coins from %d to %d",
                    coins, fromUser, toUser));
            return result;
        } catch (Exception e) {
            result = new ApiResult(ApiResult.TRANSACTION_FAILED,
                    "failed to transfer coins from " + fromUser + " to "
                    + toUser + " with error:" + e.getMessage());
            return result;
        }
    }

    @RequestMapping(method = RequestMethod.GET,
        value = "/coins/user/{userId}")
    public @ResponseBody ApiResult getCoinsOf(@PathVariable BigInteger userId) {
        ApiResult result = ApiResult.ILLEGAL_ARGS;
        if (userId.compareTo(BigInteger.ZERO) <= 0) {
            result.appendMessage("user_id should be greater than 0, " +
                    "receive " + userId);
            return result;
        }

        Integer coins = coinService.getCoinsOf(userId);
        result = ApiResult.SUCCESS;
        result.setData(coins);

        return result;
    }



}
