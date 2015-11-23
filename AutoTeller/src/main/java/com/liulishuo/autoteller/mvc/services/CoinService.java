package com.liulishuo.autoteller.mvc.services;

import com.liulishuo.autoteller.mvc.dao.CoinsDAO;
import com.liulishuo.autoteller.mvc.dao.TransferLogDAO;
import com.liulishuo.autoteller.mvc.models.UserAccount;
import org.apache.ibatis.transaction.TransactionException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigInteger;

/**
 * Created by Administrator on 2015/11/19 0019.
 */
@Service
public class CoinService {

    Logger logger = Logger.getLogger(CoinService.class);

    @Autowired
    private CoinsDAO coinsDAO;

    @Autowired
    private TransferLogDAO transferLogDAO;

    @Autowired
    private DataSourceTransactionManager transactionMgr;

    public void depositCoin(BigInteger userId, int addCoins) {
        if (addCoins <= 0) {
            throw new IllegalArgumentException("expect addCoins > 0, " +
                    "but receive addCoins:"+ String.valueOf(addCoins));
        }

        logger.info(userId + "depoistCoin " + String.valueOf(addCoins));
        if (coinsDAO.getUserAccount(userId) == null) {
            coinsDAO.createUser(userId, addCoins);
        } else {
            coinsDAO.depositCoin(userId, addCoins);
        }
    }

    public int getCoinsOf(BigInteger userId) {
        UserAccount userAccount = coinsDAO.getUserAccount(userId);
        if (null != userAccount) {
            return userAccount.getCoins();
        } else {
            throw new IllegalStateException("The user " + userId + " is not exist");
        }
    }



    public boolean transferCoins(BigInteger fromUser, BigInteger toUser, int coins) throws Exception {
        if (coins <= 0 || !hasEnoughCoins(fromUser, coins)) {
            throw new IllegalArgumentException(fromUser + " can not provide "
                    + String.valueOf(coins) + " coins!");
        }

        logger.info(fromUser + " transfer " + String.valueOf(coins) + " coins to"
               + toUser);
        TransactionDefinition defaultTxDef = new DefaultTransactionDefinition();
        TransactionStatus status = transactionMgr.getTransaction(defaultTxDef);
        try {
            coinsDAO.depositCoin(fromUser, -coins);
            coinsDAO.depositCoin(toUser, coins);
            transactionMgr.commit(status);
            transferLogDAO.logTransaction(fromUser, toUser, coins);
        } catch (Exception e) {
            transactionMgr.rollback(status);
            logger.error("transaction failed in transferCoins:"
                    + fromUser + "|" + toUser + "|" + String.valueOf(coins));
            throw e;
        }
        return true;
    }

    public void createUser(BigInteger userId) {
        coinsDAO.createUser(userId, 0);
    }

    private boolean hasEnoughCoins(BigInteger userId, long coins) {
        UserAccount userAccount = coinsDAO.getUserAccount(userId);
        if (null != userAccount) {
            return coins <= userAccount.getCoins();
        }
        return false;
    }
    public void setCoinsDAO(CoinsDAO coinsDAO) {
        this.coinsDAO = coinsDAO;
    }
}

