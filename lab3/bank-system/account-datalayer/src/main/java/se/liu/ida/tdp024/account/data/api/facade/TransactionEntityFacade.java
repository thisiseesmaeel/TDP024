package se.liu.ida.tdp024.account.data.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;

import java.util.List;

public interface TransactionEntityFacade{
    TransactionDB create(String type, long amount, String created, String status, long accountId);

    List<Transaction> findByAccountId(long accountID);
}
