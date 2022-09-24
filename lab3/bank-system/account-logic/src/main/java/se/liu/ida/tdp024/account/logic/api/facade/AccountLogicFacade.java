package se.liu.ida.tdp024.account.logic.api.facade;


import se.liu.ida.tdp024.account.data.api.entity.Account;

import java.util.List;

public interface AccountLogicFacade {

    boolean create(String personKey, String bankKey, String accountType);

    List<Account> find(String personKey);

    boolean debit(long id, long amount);

    boolean credit(long id, long amount);

}
