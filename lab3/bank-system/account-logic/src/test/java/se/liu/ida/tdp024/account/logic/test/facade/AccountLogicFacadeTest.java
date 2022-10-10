package se.liu.ida.tdp024.account.logic.test.facade;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AccountLogicFacadeTest {

    //--- Unit under test ---//
    @Mock
    AccountEntityFacadeDB accountEntityFacadeDB = new AccountEntityFacadeDB();

    @InjectMocks
    AccountLogicFacadeImpl accountLogicFacade = new AccountLogicFacadeImpl(accountEntityFacadeDB);
    public StorageFacade storageFacade;

    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

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
                when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }

            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";

                when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "1";
                String bankName = "SWEDBANK";
                String accountType = "CHECK";

                when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "1";
                String bankName = "IKANOBANKEN";
                String accountType = "SAVINGS";

                when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "2";
                String bankName = "SWEDBANK";
                String accountType = "SAVINGS";

                when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "2";
                String bankName = "SWEDBANK";
                String accountType = "CHECK";

                when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

                boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                Assert.assertEquals(true, result);
            }
            {
                String personKey = "2";
                String bankName = "IKANOBANKEN";
                String accountType = "SAVINGS";

                when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

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

                        when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

                        boolean result = accountLogicFacade.create(personKey, bankName, accountType);
                        Assert.assertTrue(result);
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

            when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

            boolean result = accountLogicFacade.create(personKey, bankName, accountType);
            fail("Should not reach this line account type is nighter \"CHECK\" nor \"SAVINGS\"");
        }catch (AccountInputParameterException e){
            return;
        } catch (AccountServiceConfigurationException | AccountEntityNotFoundException e) {
            fail("testInvalidAccountType() failed");
        }
    }

    @Test
    public void testCreateInvalidBankName() {
        try{
            String personKey = "1";
            String bankName = "FOO BANK";
            String accountType = "CHECK";

            when(accountEntityFacadeDB.create(personKey, bankName, accountType)).thenReturn(1L); // Does not matter that much here. Mock should return a valid account id

            boolean result = accountLogicFacade.create(personKey, bankName, accountType);
            fail("Should not reach this line because bank name is invalid");
        }catch (AccountEntityNotFoundException e){
            return;
        } catch (AccountServiceConfigurationException | AccountInputParameterException e) {
            fail("testInvalidAccountType() failed");
        }
    }

    @Test
    public void testCreateInvalidPersonKey() {
        try{
            String personKey = "4000";
            String bankName = "SBAB";
            String accountType = "CHECK";

            boolean result = accountLogicFacade.create(personKey, bankName, accountType);
            fail("Should not reach this line because person key is invalid");
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

            Account account1 = new AccountDB();
            Account account2 = new AccountDB();

            ArrayList<Account> mockAccounts = new ArrayList<>();
            mockAccounts.add(account1);
            mockAccounts.add(account2);

            for(String personKey: personKeys){

                when(accountEntityFacadeDB.find(personKey)).thenReturn(mockAccounts); // Mock should return a list of accounts

                List<Account> accounts = accountLogicFacade.find(personKey);
                Assert.assertEquals(true, accounts.size() >= 1);
            }

            Account mockAccount = new AccountDB();
            mockAccount.setPersonKey("1");

            when(accountEntityFacadeDB.find("1")).thenReturn(Arrays.asList(mockAccount));

            List<Account> accounts = accountLogicFacade.find("1");
            Assert.assertEquals("1", accounts.get(0).getPersonKey());

        }catch (Exception e){
            fail("testFind() failed");
        }
    }

    @Test
    public void testInvalidPersonFind(){
        try {
            String personKey = "40000";
            when(accountEntityFacadeDB.find(personKey)).thenThrow(AccountEntityNotFoundException.class); // Mock should return a list of accounts

            List<Account> accounts = accountLogicFacade.find(personKey);
            fail("Finding accounts for an invalid person should throw AccountEntityNotFoundException");

        }catch (AccountEntityNotFoundException e){
            return;
        }
        catch (Exception e){
            fail("Finding accounts for an invalid person should throw AccountEntityNotFoundException");
        }
    }

    @Test
    public void testEmptyPersonKeyFind(){
        try {
            String personKey = "";
            when(accountEntityFacadeDB.find(personKey)).thenThrow(AccountInputParameterException.class); // Mock should return a list of accounts

            List<Account> accounts = accountLogicFacade.find(personKey);
            fail("Finding accounts when person key is empty should throw AccountInputParameterException");

        }catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Finding accounts when person key is empty should throw AccountInputParameterException");
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
                when(accountEntityFacadeDB.credit(accountId, amount)).thenReturn(mockAccount);

                Account account = accountLogicFacade.credit(accountId, amount);
                Assert.assertNotEquals(null, account);
                Assert.assertEquals(100, account.getHoldings());

                mockAccount.setHoldings(300);
                when(accountEntityFacadeDB.credit(accountId, amountTwo)).thenReturn(mockAccount);

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

            when(accountEntityFacadeDB.credit(accountId, amount)).thenThrow(AccountInputParameterException.class);

            Account account = accountLogicFacade.credit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }
        catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Crediting an account should throw AccountInputParameterException if amount is negative");
        }
    }

    @Test
    public void testZeroCredit(){
        try {
            long accountId = 2;
            long amount = 0;

            when(accountEntityFacadeDB.credit(accountId, amount)).thenThrow(AccountInputParameterException.class);

            Account account = accountLogicFacade.credit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }
        catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Crediting an account should throw AccountInputParameterException if amount is zero");
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
                when(accountEntityFacadeDB.credit(accountId, 1000)).thenReturn(mockAccount);

                // Debiting account with 1000
                Account account = accountLogicFacade.credit(accountId, 1000);
                Assert.assertEquals(1000, account.getHoldings());


                mockAccount.setHoldings(900);
                when(accountEntityFacadeDB.debit(accountId, amount)).thenReturn(mockAccount);

                Account accountAfterFirstDebit = accountLogicFacade.debit(accountId, amount);
                Assert.assertNotEquals(null, accountAfterFirstDebit);
                Assert.assertEquals(900, accountAfterFirstDebit.getHoldings());


                mockAccount.setHoldings(700);
                when(accountEntityFacadeDB.debit(accountId, amountTwo)).thenReturn(mockAccount);

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

            when(accountEntityFacadeDB.debit(accountId, amount)).thenThrow(AccountInputParameterException.class);
            Account account = accountLogicFacade.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Debiting an account should throw AccountInputParameterException if amount is negative");
        }
    }

    @Test
    public void testZeroDebit(){
        try {
            long accountId = 4;
            long amount = 0;

            when(accountEntityFacadeDB.debit(accountId, amount)).thenThrow(AccountInputParameterException.class);
            Account account = accountLogicFacade.debit(accountId, amount);
            account.getHoldings();
            fail("Could not be able to reach this line");

        }catch (AccountInputParameterException e){
            return;
        }
        catch (Exception e){
            fail("Debiting an account should throw AccountInputParameterException if amount is zero");
        }
    }
}
