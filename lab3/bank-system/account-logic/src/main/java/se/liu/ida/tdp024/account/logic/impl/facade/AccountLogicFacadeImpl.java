package se.liu.ida.tdp024.account.logic.impl.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.mock.BankMock;
import se.liu.ida.tdp024.account.logic.mock.PersonMock;
import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class AccountLogicFacadeImpl implements AccountLogicFacade {
    private final AccountEntityFacade accountEntityFacade;

    private static final HTTPHelper httpHelper = new HTTPHelperImpl();

    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
    }

    @Override
    public boolean create(String personKey, String bankName, String accountType) {
        // we need to implement a logic before calling data layer
        // 1) Validate the accountType
        if (!(accountType.equals("CHECK") || accountType.equals("SAVINGS")))
            return false;

        // 2) Call to Elixir and check whether the person exists in our database or not.
        String response = httpHelper.get("http://localhost:8060/api/person/find.key", "key", personKey);
        if (response.equals("null")) {
            return false;
        }

        // Person API MOCK
        if(PersonMock.findPersonById(personKey) == null){
            return false;
        }

//        3) Call to Rust and check whether the bank exists in our database or not and get the unique bank key.
        String bankKey = httpHelper.get("http://localhost:8070/bank/find.name", "name", bankName);
        if(bankKey.equals("null")){
            return false;
        }

        // Bank API MOCK
//        String bankKey = BankMock.findBankByName(bankName);
//        if( bankKey == null){
//            return false;
//        }

        accountEntityFacade.create(personKey, bankKey, accountType);

        return true;

    }

    @Override
    public List<Account> find(String personKey) {
        // Call to Elixir and check whether the person exists in our database or not.
        String response = httpHelper.get("http://localhost:8060/api/person/find.key", "key", personKey);
        if(response.equals("null")){
            return new ArrayList<>();
        }

        // Person API MOCK
//        if(PersonMock.findPersonById(personKey) == null){
//            return new ArrayList<>();
//        }
        return accountEntityFacade.find(personKey);
    }
    @Override
    public boolean debit(long id, long amount) {
        return accountEntityFacade.debit(id, amount);
    }

    @Override
    public boolean credit(long id, long amount) {
        return accountEntityFacade.credit(id, amount);
    }


}
