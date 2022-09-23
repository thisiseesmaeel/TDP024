package se.liu.ida.tdp024.account.data.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;

import java.util.List;

public interface AccountEntityFacade {
    boolean create(String accountType, String personKey, String bankKey);

    List<Account> find(String personKey);

    boolean credit(long id);

    boolean debit(long id);

    //ArrayList<Account> transactions(long id); // not implemented yet
}
