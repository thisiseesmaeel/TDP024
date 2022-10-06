package se.liu.ida.tdp024.account.logic.api.facade;


import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.exception.InsufficientHoldingException;

import java.util.List;

public interface AccountLogicFacade {

    boolean create(String personKey, String bankKey, String accountType)
            throws AccountInputParameterException, AccountServiceConfigurationException;

    List<Account> find(String personKey)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException;

    Account debit(long id, long amount)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException,
            InsufficientHoldingException;

    Account credit(long id, long amount)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException;

}
