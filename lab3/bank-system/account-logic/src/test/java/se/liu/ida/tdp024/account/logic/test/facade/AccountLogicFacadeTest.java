package se.liu.ida.tdp024.account.logic.test.facade;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class AccountLogicFacadeTest {


    //--- Unit under test ---//
    public AccountLogicFacadeImpl accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
    public StorageFacade storageFacade;

    @After
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }

    @Test
    public void testCreate() {
        try {
            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";
                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";
                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "CHECK";
                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "1";
                String bankName = "IKANOBANKEN";
                String accountType = "SAVINGS";
                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "2";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";
                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "2";
                String bankName = "SWEDBANK";
                String accountType = "CHECK";
                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "2";
                String bankName = "IKANOBANKEN";
                String accountType = "SAVINGS";
                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
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
                        boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                        Assert.assertEquals(true, result);
                    }catch (AccountServiceConfigurationException | AccountInputParameterException |
                            AccountEntityNotFoundException e){
                        fail("Something went wrong while creating account");
                    }
                }
            }
        }

    }

    @Test
    public void testInvalidAccountType() {
        try{
            String personKey = "1";
            String bankName = "SWEDBANK";
            String accountType = "FOO";
            boolean result = accountLogicFacade.create(personKey, bankName, accountType);
            fail("Should not reach this line account type is nighter \"CHECK\" nor \"SAVINGS\"");
        }catch (AccountInputParameterException e){
            return;
        } catch (AccountServiceConfigurationException | AccountEntityNotFoundException e) {
            fail("testInvalidAccountType() failed");
        }
    }

    @Test
    public void testInvalidBankName() {
        try{
            String personKey = "1";
            String bankName = "FOO BANK";
            String accountType = "CHECK";
            boolean result = accountLogicFacade.create(personKey, bankName, accountType);
            fail("Should not reach this line because bank name is invalid");
        }catch (AccountEntityNotFoundException e){
            return;
        } catch (AccountServiceConfigurationException | AccountInputParameterException e) {
            fail("testInvalidAccountType() failed");
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

            for(String personKey: personKeys){
                List<Account> accounts = accountLogicFacade.find(personKey);
                Assert.assertEquals(true, accounts.size() >= 1);
            }

            List<Account> accounts = accountLogicFacade.find("1");
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

                Account account = accountLogicFacade.credit(accountId, amount);
                Assert.assertNotEquals(null, account);
                Assert.assertEquals(100, account.getHoldings());

                Account account1 = accountLogicFacade.credit(accountId, amountTwo);
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

            Account account = accountLogicFacade.credit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (Exception e){
            return;
        }
    }

    @Test
    public void testZeroCredit(){
        try {
            long accountId = 2;
            long amount = 0;

            Account account = accountLogicFacade.credit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (Exception e){
            return;
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
                Account account = accountLogicFacade.credit(accountId, 1000);
                Assert.assertEquals(1000, account.getHoldings());

                Account accountAfterFirstDebit = accountLogicFacade.debit(accountId, amount);
                Assert.assertNotEquals(null, accountAfterFirstDebit);
                Assert.assertEquals(900, accountAfterFirstDebit.getHoldings());

                Account accountAfterSecondDebit = accountLogicFacade.debit(accountId, amountTwo);
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

            Account account = accountLogicFacade.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (Exception e){
            return;
        }
    }

    @Test
    public void testZeroDebit(){
        try {
            long accountId = 4;
            long amount = 0;

            Account account = accountLogicFacade.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (Exception e){
            return;
        }
    }
}
