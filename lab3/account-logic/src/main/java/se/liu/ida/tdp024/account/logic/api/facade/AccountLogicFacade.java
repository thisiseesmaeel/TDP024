package se.liu.ida.tdp024.account.logic.api.facade;


import se.liu.ida.tdp024.account.data.api.entity.Account;

import java.util.List;

public interface AccountLogicFacade {

    boolean create(String accountType, String personKey, String bankKey);

    List<Account> find(String personKey);

    boolean debit(long id, long amount);

    boolean credit(long id, long amount);

}
