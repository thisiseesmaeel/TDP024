package se.liu.ida.tdp024.account.logic.test.facade;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImp;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionLogicFacadeTest {
    @Mock
    AccountEntityFacadeDB accountEntityFacadeDB;

    AccountLogicFacadeImpl accountLogicFacade;

    @Mock
    TransactionEntityFacade transactionEntityFacade;

    TransactionLogicFacadeImp transactionLogicFacadeImp;

    @Before
    public void setup(){
        accountEntityFacadeDB = mock(AccountEntityFacadeDB.class);
        accountLogicFacade = new AccountLogicFacadeImpl(accountEntityFacadeDB);

        transactionEntityFacade = mock(TransactionEntityFacade.class);
        transactionLogicFacadeImp = new TransactionLogicFacadeImp(transactionEntityFacade);
    }

    @Test
    public void testTransaction(){
        try{
            long accountId = 1;
            long amount = 100;
            List<Transaction> transactionsMock = new ArrayList<>();

            when(transactionEntityFacade.findByAccountId(accountId)).thenReturn(transactionsMock);
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

            for(int i = 0; i < 8; i++){
                transactions.add(new TransactionDB());
            }

            when(transactionEntityFacade.findByAccountId(accountId)).thenReturn(transactions);
            transactions = transactionLogicFacadeImp.findByAccountId(accountId);

            Assert.assertEquals(8, transactions.size());
        }catch (Exception e){
            fail("testTransaction failed");
        }
    }

}
