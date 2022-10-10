package se.liu.ida.tdp024.account.data.test.facade;


import org.junit.Assert;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.exception.InsufficientHoldingException;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;

import org.junit.After;
import org.junit.Test;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AccountEntityFacadeTest {

    //---- Unit under test ----//
    private final AccountEntityFacadeDB accountEntityFacadeDB = new AccountEntityFacadeDB();
    private StorageFacade storageFacade;

    @After
    public void tearDown() {
       // storageFacade.emptyStorage();
    }

    @Test
    public void testCreate() {
        try {
            {
                String personKey = "1";
                String bankKey = "1";
                String accountType = "SAVINGS";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                Assert.assertNotEquals(-1, result);
            }
            {
                String personKey = "1";
                String bankKey = "1";
                String accountType = "SAVINGS";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                Assert.assertNotEquals(-1, result);
            }
            {
                String personKey = "1";
                String bankKey = "1";
                String accountType = "CHECK";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                Assert.assertNotEquals(-1, result);
            }
            {
                String personKey = "1";
                String bankKey = "2";
                String accountType = "SAVINGS";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                Assert.assertNotEquals(-1, result);
            }
            {
                String personKey = "2";
                String bankKey = "1";
                String accountType = "SAVINGS";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                Assert.assertNotEquals(-1, result);
            }
            {
                String personKey = "2";
                String bankKey = "1";
                String accountType = "CHECK";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                Assert.assertNotEquals(-1, result);
            }
            {
                String personKey = "2";
                String bankKey = "2";
                String accountType = "SAVINGS";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                Assert.assertNotEquals(-1, result);
            }

        } catch (AccountInputParameterException | AccountServiceConfigurationException e ) {
            fail("Could not create an account.")
;       }catch (Exception e){
            fail("Something went wrong. Cause => " + e.getMessage());
        }
    }

    @Test
    public void createSuccessAllCombos() {

        List<String> personKeys = new ArrayList<String>();
        List<String> bankKeys = new ArrayList<String>();
        List<String> accountTypes = new ArrayList<String>();

        personKeys.add("1");
        personKeys.add("2");
        personKeys.add("3");
        personKeys.add("4");
        personKeys.add("5");

        bankKeys.add("1");
        bankKeys.add("2");
        bankKeys.add("3");
        bankKeys.add("4");
        bankKeys.add("5");
        bankKeys.add("6");
        bankKeys.add("7");
        bankKeys.add("8");
        bankKeys.add("9");

        accountTypes.add("CHECK");
        accountTypes.add("SAVINGS");

        for (String personKey : personKeys) {
            for (String bankKey : bankKeys) {
                for (String accountType : accountTypes) {
                    try {
                        long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                        Assert.assertNotEquals(-1, result);
                    }catch (AccountServiceConfigurationException | AccountInputParameterException e){
                        fail("Something went wrong while creating account");
                    }
                }
            }
        }

    }

    @Test
    public void createInvalid() {
        try {
            {
                String personKey = "";
                String bankKey = "1";
                String accountType = "CHECK";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                fail("Should not reach this line");
            }
        }
        catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Should throw AccountInputParameterException while parameters are empty");
        }

        try {
            {
                String personKey = "2";
                String bankKey = "";
                String accountType = "CHECK";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                fail("Should not reach this line");
            }
        }
        catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Should throw AccountInputParameterException while parameters are empty");
        }

        try {
            {
                String personKey = "1";
                String bankKey = "2";
                String accountType = "";
                long result = accountEntityFacadeDB.create(personKey, bankKey, accountType);
                fail("Should not reach this line");
            }
        }
        catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Should throw AccountInputParameterException while parameters are empty");
        }

    }

    @Test
    public void testFindOutOfRangePersonKey(){
        try {
            String personKey = "1000"; // this person key is out of range
            List<Account> accounts = accountEntityFacadeDB.find(personKey);
            fail("Should not reach this line due to invalid person key");

        }catch (AccountEntityNotFoundException e){
            return;
        }
        catch (Exception e){
            fail("Finding a person with person key 1000 should lead to AccountEntityNotFoundException");
        }
    }

    @Test
    public void testFindEmptyPersonKey(){
        try {
            String personKey = ""; // this person key is invalid
            List<Account> accounts = accountEntityFacadeDB.find(personKey);
            fail("Should not reach this line due to empty person key");

        }catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Finding a person with person key 1000 should lead to AccountInputParameterException");
        }
    }

    @Test
    public void testFindAllPersonKeys(){
        try {
            // We have added all combinations here we check if we
            // can find all accounts each person MUST at least one account
            List<String> personKeys = new ArrayList<String>();
            personKeys.add("1");
            personKeys.add("2");
            personKeys.add("3");
            personKeys.add("4");
            personKeys.add("5");

            for(String personKey: personKeys){
                List<Account> accounts = accountEntityFacadeDB.find(personKey);
                Assert.assertTrue(accounts.size() >= 1);
                for(Account account: accounts){
                    Assert.assertTrue(account.getAccountType().equals("CHECK") || account.getAccountType().equals("SAVINGS"));
                    Assert.assertTrue(account.getBankKey().equals("1")
                            || account.getBankKey().equals("2")
                            || account.getBankKey().equals("3")
                            || account.getBankKey().equals("4")
                            || account.getBankKey().equals("5")
                            || account.getBankKey().equals("6")
                            || account.getBankKey().equals("7")
                            || account.getBankKey().equals("8")
                            || account.getBankKey().equals("9"));
                }
            }


            List<Account> accounts = accountEntityFacadeDB.find("1");
            Assert.assertEquals("1", accounts.get(0).getPersonKey());


        }catch (Exception e){
            fail("testFind() failed");
        }

    }

    @Test
    public void testCredit(){
        try {
            {
                long accountId = 1;
                long amount = 100;
                long amountTwo = 200;

                Account account = accountEntityFacadeDB.credit(accountId, amount);
                Assert.assertNotEquals(null, account);
                Assert.assertEquals(100, account.getHoldings());

                Account account1 = accountEntityFacadeDB.credit(accountId, amountTwo);
                Assert.assertNotEquals(null, account1);
                Assert.assertEquals(300, account.getHoldings());
            }
        }catch (Exception e){
            fail("testCredit() failed");
        }
    }

    @Test
    public void testNegativeCredit(){
        try {
            long accountId = 2;
            long amount = -100;

            Account account = accountEntityFacadeDB.credit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Crediting an account with negative amount should throw AccountInputParameterException");
        }
    }

    @Test
    public void testZeroCredit(){
        try {
            long accountId = 2;
            long amount = 0;

            Account account = accountEntityFacadeDB.credit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Crediting an account with zero amount should throw AccountInputParameterException");
        }
    }

    @Test
    public void testInvalidAccountCredit(){
        try {
            long accountId = 4000; // this account does not exist
            long amount = 100;

            Account account = accountEntityFacadeDB.credit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }
        catch (AccountEntityNotFoundException e){
            return;
        }
        catch (Exception e){
            fail("Crediting an invalid account should throw AccountEntityNotFoundException");
        }
    }

    @Test
    public void testDebit(){
        try {
            {
                long accountId = 3;
                long amount = 100;
                long amountTwo = 200;

                // Debiting account with 1000
                Account account = accountEntityFacadeDB.credit(accountId, 1000);
                Assert.assertEquals(1000, account.getHoldings());

                Account accountAfterFirstDebit = accountEntityFacadeDB.debit(accountId, amount);
                Assert.assertNotEquals(null, accountAfterFirstDebit);
                Assert.assertEquals(900, accountAfterFirstDebit.getHoldings());

                Account accountAfterSecondDebit = accountEntityFacadeDB.debit(accountId, amountTwo);
                Assert.assertNotEquals(null, accountAfterSecondDebit);
                Assert.assertEquals(700, accountAfterSecondDebit.getHoldings());
            }
        }catch (Exception e){
            fail("testDebit() failed");
        }
    }

    @Test
    public void testNegativeDebit(){
        try {
            long accountId = 4;
            long amount = -100;

            Account account = accountEntityFacadeDB.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (Exception e){
            return;
        }
    }

    @Test
    public void testInvalidAccountDebit(){
        try {
            long accountId = 4000; // this account id is not yet reached
            long amount = 100;

            Account account = accountEntityFacadeDB.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (AccountEntityNotFoundException e){
            return;
        }
        catch (Exception e){
            fail("Debiting an account that does not exist should throw AccountEntityNotFoundException");
        }
    }

    @Test
    public void testDebitEmptyAccount(){
        try {
            long accountId = 4;
            long amount = 100;

            Account account = accountEntityFacadeDB.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (InsufficientHoldingException e){
            return;
        }
        catch (Exception e){
            fail("Debiting an account that has insufficient balance should throw InsufficientHoldingException");
        }
    }

    @Test
    public void testZeroDebit(){
        try {
            long accountId = 4;
            long amount = 0;

            Account account = accountEntityFacadeDB.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (Exception e){
            return;
        }
    }
}