package se.liu.ida.tdp024.account.logic.impl.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;

import java.util.List;


public class AccountLogicFacadeImpl implements AccountLogicFacade {
    private AccountEntityFacade accountEntityFacade;
    
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    @Override
    public boolean create(String accountType, String personKey, String bankKey) {
        // we need to implement a logic before calling data layer
        // Call to Elixir and check whether the person exists in our database or not.
        // Call to Rust and check whether the bank exists in our database or not.
        accountEntityFacade.create(accountType, personKey, bankKey);

        return false;
    }

    @Override
    public List<Account> find(String personKey) {
        // Call to Elixir and check whether the person exists in our database or not.
        return accountEntityFacade.find(personKey);
    }

    @Override
    public boolean credit(long id) {
        return false;
    }

    @Override
    public boolean debit(long id) {
        return false;
    }
}
