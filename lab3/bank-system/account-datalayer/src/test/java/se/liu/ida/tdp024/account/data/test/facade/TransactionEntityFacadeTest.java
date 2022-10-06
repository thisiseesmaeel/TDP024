package se.liu.ida.tdp024.account.data.test.facade;

import org.junit.Assert;
import org.junit.Test;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;

import java.util.List;

import static org.junit.Assert.fail;

public class TransactionEntityFacadeTest {
    private final AccountEntityFacadeDB accountEntityFacadeDB = new AccountEntityFacadeDB();
    private final TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();

    @Test
    public void testTransaction(){
        try{
            // This account id has no transaction for sure
            long accountId = 10;
            long amount = 100;
            List<Transaction> transactions = transactionEntityFacade.findByAccountId(accountId);
            Assert.assertEquals(0, transactions.size());

            accountEntityFacadeDB.credit(accountId, amount);
            accountEntityFacadeDB.credit(accountId, amount);
            accountEntityFacadeDB.credit(accountId, amount);
            accountEntityFacadeDB.credit(accountId, amount);

            accountEntityFacadeDB.debit(accountId, amount);
            accountEntityFacadeDB.debit(accountId, amount);
            accountEntityFacadeDB.debit(accountId, amount);
            accountEntityFacadeDB.debit(accountId, amount);

            transactions = transactionEntityFacade.findByAccountId(accountId);

            Assert.assertEquals(8, transactions.size());
        }catch (Exception e){
            fail("testTransaction failed");
        }
    }
}
