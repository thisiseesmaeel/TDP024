package se.liu.ida.tdp024.account.logic.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;

import java.util.List;

public interface TransactionLogicFacade {

//    boolean create(String type, long amount, String status, long accountId);

    List<Transaction> findByAccountId(long accountID)
            throws AccountServiceConfigurationException;
}
