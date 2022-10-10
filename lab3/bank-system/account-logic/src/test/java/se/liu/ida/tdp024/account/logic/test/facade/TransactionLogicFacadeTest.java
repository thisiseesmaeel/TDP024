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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TransactionLogicFacadeTest {
    @Mock
    AccountEntityFacadeDB accountEntityFacadeDB = new AccountEntityFacadeDB();
    @InjectMocks
    private final AccountLogicFacadeImpl accountLogicFacade = new AccountLogicFacadeImpl(accountEntityFacadeDB);

    @Mock
    TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
    @InjectMocks
    private final TransactionLogicFacadeImp transactionLogicFacadeImp = new TransactionLogicFacadeImp(transactionEntityFacade);

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransaction(){
        try{
            long accountId = 10;
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

            for(int i = 0; i < 4; i++){
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
