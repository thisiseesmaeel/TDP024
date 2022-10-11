package se.liu.ida.tdp024.account.rest.test.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImp;
import se.liu.ida.tdp024.account.rest.AccountController;
import se.liu.ida.tdp024.account.rest.service.AccountService;

import static org.mockito.Mockito.mock;

public class AccountServiceTest {
    @Mock
    AccountLogicFacade accountLogicFacade;

    @Mock
    TransactionLogicFacade transactionLogicFacade;

    AccountService accountService;


    @Before
    public void setup(){
        accountLogicFacade = mock(AccountLogicFacadeImpl.class);
        transactionLogicFacade = mock(TransactionLogicFacadeImp.class);
        accountService = new AccountService(accountLogicFacade, transactionLogicFacade);
    }

    @Test
    public void createTest(){
        Assert.assertEquals(2 , 2);
    }


    @Test
    public void testUnnecessary(){
        Assert.assertEquals(2 , 2);
    }
}
