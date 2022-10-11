package se.liu.ida.tdp024.account.rest.test.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImp;
import se.liu.ida.tdp024.account.rest.service.AccountService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void testCreate() {
        try {
            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";
                when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
            }

            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";

                when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
            }
            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "CHECK";

                when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
            }
            {
                String personKey = "1";
                String bankName = "IKANOBANKEN";
                String accountType = "SAVINGS";

                when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
            }
            {
                String personKey = "2";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";

                when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
            }
            {
                String personKey = "2";
                String bankName = "SWEDBANK";
                String accountType = "CHECK";

                when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
            }
            {
                String personKey = "2";
                String bankName = "IKANOBANKEN";
                String accountType = "SAVINGS";

                when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
            }

        } catch (AccountInputParameterException | AccountServiceConfigurationException e ) {
            fail(e.getMessage());
        }catch (Exception e){
            fail("Something went wrong. Cause => " + e.getMessage());
        }
    }

    @Test
    public void createSuccessAllCombos() {

        List<String> personKeys = new ArrayList<String>();
        List<String> bankNames = new ArrayList<String>();
        List<String> accountTypes = new ArrayList<String>();

        personKeys.add("1");
        personKeys.add("2");
        personKeys.add("3");
        personKeys.add("4");
        personKeys.add("5");

        bankNames.add("SWEDBANK");
        bankNames.add("IKANOBANKEN");
        bankNames.add("JPMORGAN");
        bankNames.add("NORDEA");
        bankNames.add("CITIBANK");
        bankNames.add("HANDELSBANKEN");
        bankNames.add("SBAB");
        bankNames.add("HSBC");
        bankNames.add("NORDNET");

        accountTypes.add("CHECK");
        accountTypes.add("SAVINGS");

        accountTypes.add("CHECK");
        accountTypes.add("SAVINGS");

        for (String personKey : personKeys) {
            for (String bankName : bankNames) {
                for (String accountType : accountTypes) {
                    try {

                        when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(true);

                        ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
                        Assert.assertEquals(HttpStatus.OK, result.getStatusCode());
                    }catch (AccountServiceConfigurationException | AccountInputParameterException |
                            AccountEntityNotFoundException e){
                        fail("Something went wrong while creating account");
                    }
                }
            }
        }

    }

    @Test
    public void testCreateInvalidAccountType() {
        try{
            String personKey = "1";
            String bankName = "SWEDBANK";
            String accountType = "FOO";

            when(accountLogicFacade.create(personKey, bankName, accountType)).thenThrow(AccountInputParameterException.class);

            ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);

            Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        }catch (AccountInputParameterException e){
            return;
        } catch (Exception e) {
            fail("Should not throw Exception");
        }
    }

    @Test
    public void testCreateInvalidBankName() {
        try{
            String personKey = "1";
            String bankName = "FOO BANK";
            String accountType = "CHECK";

            when(accountLogicFacade.create(personKey, bankName, accountType)).thenThrow(AccountEntityNotFoundException.class);

            ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
            Assert.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        } catch (Exception e) {
            fail("Should not throw Exception");
        }
    }

    @Test
    public void testCreateInvalidPersonKey() {
        try{
            String personKey = "4000";
            String bankName = "SBAB";
            String accountType = "CHECK";

            when(accountLogicFacade.create(personKey, bankName, accountType)).thenThrow(AccountEntityNotFoundException.class);

            ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
            Assert.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }catch (Exception e) {
            fail("Should not throw Exception");
        }
    }

    @Test
    public void testCreateInternalServerError() {
        try{
            String personKey = "1";
            String bankName = "SBAB";
            String accountType = "CHECK";

            when(accountLogicFacade.create(personKey, bankName, accountType)).thenThrow(AccountServiceConfigurationException.class);

            ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
            Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        }catch (Exception e) {
            fail("Should not throw Exception");
        }
    }

    @Test
    public void testCreatFacadeReruntFalse() {
        try{
            String personKey = "1";
            String bankName = "SBAB";
            String accountType = "CHECK";

            when(accountLogicFacade.create(personKey, bankName, accountType)).thenReturn(false);

            ResponseEntity<String> result = accountService.create(personKey, bankName, accountType);
            Assert.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }catch (Exception e) {
            fail("Should not throw Exception");
        }
    }
    @Test
    public void testFind(){
        try {
            // We have added all combinations here we check if we
            // can find all accounts each person MUST at least one account
            List<String> personKeys = new ArrayList<String>();
            personKeys.add("1");
            personKeys.add("2");
            personKeys.add("3");
            personKeys.add("4");
            personKeys.add("5");

            Account account1 = new AccountDB();
            Account account2 = new AccountDB();

            ArrayList<Account> mockAccounts = new ArrayList<>();
            mockAccounts.add(account1);
            mockAccounts.add(account2);

            for(String personKey: personKeys){

                when(accountLogicFacade.find(personKey)).thenReturn(mockAccounts); // Mock should return a list of accounts

                ResponseEntity<List<Account>> accounts = accountService.findPerson("1");
                Assert.assertTrue(accounts.getBody().size() >= 1);
            }

            Account mockAccount = new AccountDB();
            mockAccount.setPersonKey("1");

            when(accountLogicFacade.find("1")).thenReturn(Arrays.asList(mockAccount));

            ResponseEntity<List<Account>> accounts = accountService.findPerson("1");
            Assert.assertEquals("1", accounts.getBody().get(0).getPersonKey());

        }catch (Exception e){
            fail("testFind() failed");
        }
    }

    @Test
    public void testInvalidPersonFind(){
        try {
            String personKey = "40000";
            when(accountLogicFacade.find(personKey)).thenThrow(AccountEntityNotFoundException.class);

            ResponseEntity result = accountService.findPerson(personKey);
            Assert.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        }
        catch (Exception e){
            fail("Should not throw exception " + e.getMessage());
        }
    }

    @Test
    public void testInternalServerErrorInFind(){
        try {
            String personKey = "1";
            when(accountLogicFacade.find(personKey)).thenThrow(AccountServiceConfigurationException.class);

            ResponseEntity result = accountService.findPerson(personKey);
            Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        }
        catch (Exception e){
            fail("Should not throw exception " + e.getMessage());
        }
    }

    @Test
    public void testEmptyPersonKeyFind(){
        try {
            String personKey = "";
            when(accountLogicFacade.find(personKey)).thenThrow(AccountInputParameterException.class);

            ResponseEntity<List<Account>> response = accountService.findPerson(personKey);
            Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }catch (Exception e){
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCredit(){
        try {
            {
                long accountId = 1;
                long amount = 100;
                long amountTwo = 200;

                Account mockAccount = new AccountDB();
                mockAccount.setHoldings(100);
                when(accountLogicFacade.credit(accountId, amount)).thenReturn(mockAccount);

                ResponseEntity response = accountService.credit(accountId, amount);
                Assert.assertEquals(HttpStatus.OK, response.getStatusCode());

                mockAccount.setHoldings(300);
                when(accountLogicFacade.credit(accountId, amountTwo)).thenReturn(mockAccount);

                ResponseEntity response1 = accountService.credit(accountId, amountTwo);
                Assert.assertEquals(HttpStatus.OK, response1.getStatusCode());
            }
        }catch (Exception e){
            fail("Should not throw exception ");
        }
    }

    @Test
    public void testNegativeCredit(){
        try {
            long accountId = 2;
            long amount = -100;

            when(accountLogicFacade.credit(accountId, amount)).thenThrow(AccountInputParameterException.class);

            ResponseEntity response = accountService.credit(accountId, amount);
            Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
        catch (Exception e){
            fail("Should not throw exception ");
        }
    }

    @Test
    public void testZeroCredit(){
        try {
            long accountId = 2;
            long amount = 0;

            when(accountLogicFacade.credit(accountId, amount)).thenThrow(AccountInputParameterException.class);

            ResponseEntity response = accountService.credit(accountId, amount);
            Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        }catch (Exception e){
            fail("Should not throw exception ");
        }
    }
    @Test
    public void testNotFoundInCredit(){
        try {
            long accountId = 4000; // Invalid id for now
            long amount = 100;

            when(accountLogicFacade.credit(accountId, amount)).thenThrow(AccountEntityNotFoundException.class);

            ResponseEntity response = accountService.credit(accountId, amount);
            Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
        catch (Exception e){
            fail("Should not throw exception ");
        }
    }

    @Test
    public void testInternalExceptionCredit(){
        try {
            long accountId = 2;
            long amount = 100;

            when(accountLogicFacade.credit(accountId, amount)).thenThrow(AccountServiceConfigurationException.class);

            ResponseEntity response = accountService.credit(accountId, amount);
            Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
        catch (Exception e){
            fail("Should not throw exception ");
        }
    }

    @Test
    public void testDebit(){
        try {
            {
                long accountId = 3;
                long amount = 100;
                long amountTwo = 200;

                Account mockAccount = new AccountDB();
                mockAccount.setHoldings(1000);
                when(accountLogicFacade.credit(accountId, 1000)).thenReturn(mockAccount);

                // Crediting account with 1000
                ResponseEntity response = accountService.credit(accountId, 1000);
                Assert.assertEquals(HttpStatus.OK, response.getStatusCode());


                mockAccount.setHoldings(900);
                // Debiting account with 100
                when(accountLogicFacade.debit(accountId, amount)).thenReturn(mockAccount);

                ResponseEntity response2 = accountService.debit(accountId, amount);
                Assert.assertEquals(HttpStatus.OK, response2.getStatusCode());


                mockAccount.setHoldings(700);
                // Debiting account with 200
                when(accountLogicFacade.debit(accountId, amountTwo)).thenReturn(mockAccount);

                ResponseEntity response3 = accountService.debit(accountId, amountTwo);
                Assert.assertEquals(HttpStatus.OK, response3.getStatusCode());
            }
        }catch (Exception e){
            fail("testDebit() failed");
        }
    }

    @org.junit.Test
    public void testNegativeDebit(){
        try {
            long accountId = 4;
            long amount = -100;

            when(accountLogicFacade.debit(accountId, amount)).thenThrow(AccountInputParameterException.class);
            ResponseEntity<String> response = accountService.debit(accountId, amount);
            Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        }catch (Exception e){
            fail("Should not throw exception ");
        }
    }

    @Test
    public void testZeroDebit(){
        try {
            long accountId = 4;
            long amount = 0;

            when(accountLogicFacade.debit(accountId, amount)).thenThrow(AccountInputParameterException.class);
            ResponseEntity<String> response = accountService.debit(accountId, amount);
            Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }catch (Exception e){
            fail("Should not throw exception ");
        }
    }

    @Test
    public void testTransaction(){
        try {
            long accountId = 4;

            List<Transaction> mockTransactions = new ArrayList<>();
            Transaction t1 = new TransactionDB();
            Transaction t2 = new TransactionDB();

            mockTransactions.add(t1);
            mockTransactions.add(t2);

            when(transactionLogicFacade.findByAccountId(accountId)).thenReturn(mockTransactions);
            ResponseEntity<List<Transaction>> transactions = accountService.transaction(accountId);
            Assert.assertTrue(transactions.getBody().size() >= 2);
        }catch (Exception e){
            fail("Should not throw exception ");
        }
    }

    @Test
    public void testTransactionFailure(){
        try {
            long accountId = 4;

            List<Transaction> mockTransactions = new ArrayList<>();
            Transaction t1 = new TransactionDB();
            Transaction t2 = new TransactionDB();

            mockTransactions.add(t1);
            mockTransactions.add(t2);

            when(transactionLogicFacade.findByAccountId(accountId)).thenThrow(AccountServiceConfigurationException.class);
            ResponseEntity<List<Transaction>> transactions = accountService.transaction(accountId);
            Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, transactions.getStatusCode());
        }
        catch (Exception e){
            fail("Should not throw exception");
        }
    }
}
