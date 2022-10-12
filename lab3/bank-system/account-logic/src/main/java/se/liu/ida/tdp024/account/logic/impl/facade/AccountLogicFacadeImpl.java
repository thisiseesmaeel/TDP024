package se.liu.ida.tdp024.account.logic.impl.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.exception.InsufficientHoldingException;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.mock.BankMock;
import se.liu.ida.tdp024.account.logic.mock.PersonMock;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;

import java.util.List;


public class AccountLogicFacadeImpl implements AccountLogicFacade {
    private final AccountEntityFacade accountEntityFacade;

    private static final HTTPHelper httpHelper = new HTTPHelperImpl();

    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    @Override
    public boolean create(String personKey, String bankName, String accountType)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException{
        // we need to implement a logic before calling data layer

        // 1) Validate the accountType
        if(!(accountType.equals("CHECK") || accountType.equals("SAVINGS")))
            throw new AccountInputParameterException("Account type is incorrect.");

        // 2) Call to Elixir and check whether the person exists in our database or not.
        String response = httpHelper.get("http://localhost:8060/api/person/find.key", "key", personKey);
        if (response.equals("null")) {
            throw new AccountEntityNotFoundException("Could not find this person.");
        }

        // Person API MOCK
//        String response = PersonMock.findPersonById(personKey);
//        if(response == null){
//            throw new AccountEntityNotFoundException("Could not find this person.");
//        }

        // 3) Call to Rust and check whether the bank exists in our database or not and get the unique bank key.
        String bankKey = httpHelper.get("http://localhost:8070/bank/find.name", "name", bankName);
        if(bankKey.equals("null")){
            throw new AccountEntityNotFoundException("Could not find this bank.");
        }

        // Bank API MOCK
//        String bankKey = BankMock.findBankByName(bankName);
//        if( bankKey == null){
//            throw new AccountEntityNotFoundException("Could not find this bank.");
//        }

        try {
            accountEntityFacade.create(personKey, bankKey, accountType);
        }
        catch(AccountInputParameterException | AccountServiceConfigurationException e){
            throw e;
        }

        return true;
    }

    @Override
    public List<Account> find(String personKey)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException{
        try {
            // Call to Elixir and check whether the person exists in our database or not.
            String response = httpHelper.get("http://localhost:8060/api/person/find.key", "key", personKey);
            if (response.equals("null")) {
                throw new AccountEntityNotFoundException("Could not find this person.");
            }

            return accountEntityFacade.find(personKey);
        }
        catch(AccountEntityNotFoundException | AccountInputParameterException| AccountServiceConfigurationException e){
            throw e;
        }
    }
    @Override
    public Account debit(long id, long amount)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException,
            InsufficientHoldingException {
        try {
            return accountEntityFacade.debit(id, amount);
        }catch (AccountEntityNotFoundException | AccountInputParameterException | AccountServiceConfigurationException e){
            throw e;
        }
    }

    @Override
    public Account credit(long id, long amount)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException {
        try {
            return accountEntityFacade.credit(id, amount);
        }
        catch (AccountEntityNotFoundException | AccountInputParameterException | AccountServiceConfigurationException e){
            throw e;
        }
    }

}
