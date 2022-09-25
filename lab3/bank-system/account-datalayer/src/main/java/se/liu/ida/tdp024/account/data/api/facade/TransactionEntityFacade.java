package se.liu.ida.tdp024.account.data.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;

public interface TransactionEntityFacade{
    boolean create(String type, long amount, String status, long accountId);
}
