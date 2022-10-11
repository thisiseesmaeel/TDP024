package se.liu.ida.tdp024.account.logic.impl.facade;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;

import java.util.List;

public class TransactionLogicFacadeImp implements TransactionLogicFacade {
    private final TransactionEntityFacade transactionEntityFacade;

    public TransactionLogicFacadeImp(TransactionEntityFacade transactionEntityFacade) {
        this.transactionEntityFacade = transactionEntityFacade;
    }

//    @Override
//    public boolean create(String type, long amount, String status, long accountId) {
//        transactionEntityFacade.create(type, amount, status, accountId);
//        return true;
//    }

    @Override
    public List<Transaction> findByAccountId(long accountID)
    throws AccountServiceConfigurationException {
        try {
            return transactionEntityFacade.findByAccountId(accountID);
        }
        catch (AccountServiceConfigurationException e){
            throw e;
        }
    }
}
