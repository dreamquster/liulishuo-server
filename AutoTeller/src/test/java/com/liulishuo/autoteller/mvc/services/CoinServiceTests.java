package com.liulishuo.autoteller.mvc.services;

import com.liulishuo.autoteller.mvc.dao.CoinsDAO;
import org.apache.ibatis.transaction.TransactionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Method;
import java.math.BigInteger;

/**
 * Created by Administrator on 2015/11/20 0020.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/test/resources/mvc-dispatcher-servlet.xml")
public class CoinServiceTests {

    @Autowired
    private CoinService coinService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private WebApplicationContext wac;

    private BigInteger fromUser = new BigInteger("3");

    private BigInteger toUser = new BigInteger("5");

    private final int FAILED_COINS = 1024;
    @Before
    public void setUp() {
        CoinsDAO coinsDAO = wac.getBean(CoinsDAO.class);
        if (coinsDAO.getUserAccount(fromUser) == null) {
            coinsDAO.createUser(fromUser, 1025);
        }
        if (coinsDAO.getUserAccount(toUser) == null) {
            coinsDAO.createUser(toUser, 3);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void depositMinusCoinTest() {
        coinService.depositCoin(new BigInteger("2"), -6);
    }

    @Transactional
    @Test
    public void depositQueryTest() {
        BigInteger userId = new BigInteger("4");
        // create a user and deposit coins
        coinService.depositCoin(userId, 4);
        long coin = coinService.getCoinsOf(userId);
        Assert.assertEquals(4, coin);

        coinService.depositCoin(userId, 5);
        coin = coinService.getCoinsOf(userId);
        Assert.assertEquals(9, coin);
    }

    public class CoinsDAOAdvice implements MethodBeforeAdvice {
        @Override
        public void before(Method method, Object[] args, Object o) throws Throwable {
            if (o instanceof CoinsDAO
                 && method.getName().equals("depositCoin")) {
                    BigInteger userId = (BigInteger)args[0];
                    Integer coins = (Integer)args[1];
                    if (userId.equals(toUser) && coins == FAILED_COINS) {
                        throw  new TransactionException("failed transaction");
                    }
            }
        }
    }

    @Test
    public void failTransferTest() throws Exception {
        CoinsDAO coinsDAO = wac.getBean(CoinsDAO.class);
        ProxyFactory factory = new ProxyFactory(coinsDAO);
        factory.addAdvice(new CoinsDAOAdvice());
        CoinsDAO mockCoinsDAO = (CoinsDAO)factory.getProxy();
        coinService.setCoinsDAO(mockCoinsDAO);

        try {
            coinService.transferCoins(fromUser, toUser, FAILED_COINS);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (TransactionException e) {
            long coins = coinService.getCoinsOf(toUser);
            Assert.assertEquals(3, coins);
            coins = coinService.getCoinsOf(fromUser);
            Assert.assertEquals(1025, coins);
        } catch (Exception e) {
            throw e;
        }
        coinService.setCoinsDAO(coinsDAO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromUserHasNotEnoughCoinsTest() throws Exception {
        coinService.transferCoins(fromUser, toUser, 1026);
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongTransferDirectionTest() throws Exception {
        coinService.transferCoins(fromUser, toUser, -9);
    }

    @Test
    public void getCoinsOfNotExistUserTest() {
        int coins = coinService.getCoinsOf(new BigInteger("1024"));
        Assert.assertEquals(0, coins);
    }

    @Transactional
    @Test
    public void transferOkTest() throws Exception {
        coinService.transferCoins(fromUser, toUser, 3);
        long fromCoins = coinService.getCoinsOf(fromUser);
        Assert.assertEquals(1022, fromCoins);
        long toCoins = coinService.getCoinsOf(toUser);
        Assert.assertEquals(6, toCoins);
    }
}

