package se.liu.ida.tdp024.account.logic.impl.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.mock.BankMock;
import se.liu.ida.tdp024.account.logic.mock.PersonMock;

import java.util.List;


public class AccountLogicFacadeImpl implements AccountLogicFacade {
    private final AccountEntityFacade accountEntityFacade;

    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    @Override
    public boolean create(String personKey, String bankName, String accountType) {
        // we need to implement a logic before calling data layer
        // TODO:
        // 1) Validate the accountType
        if(!(accountType.equals("CHECK") || accountType.equals("SAVINGS")))
            return false;

        // 2) Call to Elixir and check whether the person exists in our database or not.
        if(PersonMock.findPersonById(personKey) == null){
            return false;
        }

        // 3) Call to Rust and check whether the bank exists in our database or not and get the unique bank key.
        String bankKey = BankMock.findBankByName(bankName);
        if( bankKey == null){
            return false;
        }
        accountEntityFacade.create(personKey, bankKey, accountType);

        return true;
    }

    @Override
    public List<Account> find(String personKey) {
        // Call to Elixir and check whether the person exists in our database or not.
        return accountEntityFacade.find(personKey);
    }
    @Override
    public boolean debit(long id, long amount) {
        accountEntityFacade.debit(id, amount);
        return true;
    }

    @Override
    public boolean credit(long id, long amount) {
        accountEntityFacade.credit(id, amount);
        return true;
    }


}
