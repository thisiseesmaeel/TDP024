package se.liu.ida.tdp024.account.logic.test.facade;

import org.junit.Assert;
import org.junit.Test;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImp;

import java.util.List;

import static org.junit.Assert.fail;

public class TransactionLogicFacadeTest {
    private final AccountLogicFacadeImpl accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
    private final TransactionLogicFacadeImp transactionLogicFacadeImp = new TransactionLogicFacadeImp(new TransactionEntityFacadeDB());

    @Test
    public void testTransaction(){
        try{
            // This account id has no transaction for sure
            long accountId = 10;
            long amount = 100;
            List<Transaction> transactions = transactionLogicFacadeImp.findByAccountId(accountId);
            Assert.assertEquals(0, transactions.size());

            accountLogicFacade.credit(accountId, amount);
            accountLogicFacade.credit(accountId, amount);
            accountLogicFacade.credit(accountId, amount);
            accountLogicFacade.credit(accountId, amount);

            accountLogicFacade.debit(accountId, amount);
            accountLogicFacade.debit(accountId, amount);
            accountLogicFacade.debit(accountId, amount);
            accountLogicFacade.debit(accountId, amount);

            transactions = transactionLogicFacadeImp.findByAccountId(accountId);

            Assert.assertEquals(8, transactions.size());
        }catch (Exception e){
            fail("testTransaction failed");
        }
    }
}
