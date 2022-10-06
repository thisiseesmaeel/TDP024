package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.exception.InsufficientHoldingException;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountEntityFacadeDB implements AccountEntityFacade {
    private final EntityManager em = EMF.getEntityManager();
    @Override
    public boolean create(String personKey, String bankKey, String accountType)
            throws AccountInputParameterException, AccountServiceConfigurationException {
        try {
            if(personKey.isEmpty() || bankKey.isEmpty() || accountType.isEmpty()){
                throw new AccountInputParameterException("Creating account failed due to invalid input.");
            }

            em.getTransaction().begin();

            AccountDB accountDB = new AccountDB();
            accountDB.setPersonKey(personKey);
            accountDB.setAccountType(accountType);
            accountDB.setBankKey(bankKey);
            accountDB.setHoldings(0); // An empty account with zero holdings
            accountDB.setTransactions(new ArrayList<>());

            em.persist(accountDB);
            em.getTransaction().commit();
        }
        catch (AccountInputParameterException e){
            throw e;
        }
        catch (Exception e){
            throw new AccountServiceConfigurationException("Creating account failed due to internal service error.");
        }
        return true;
    }

    @Override
    public List<Account> find(String personKey)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException{
        try {
            if(personKey.isEmpty())
                throw new AccountInputParameterException("Finding account(s) failed due to invalid input(s).");

            Query query = em.createQuery(
                    "SELECT account FROM AccountDB account WHERE account.personKey = :personKey"
            );

            query.setParameter("personKey", personKey);
            List<Account> accounts = (List<Account>) query.getResultList();
            if (accounts.isEmpty())
                throw new AccountEntityNotFoundException("Could not found any account with this person key.");
            return accounts;
        }
        catch (AccountInputParameterException e){
            throw e;
        }
        catch (AccountEntityNotFoundException e){
            throw e;
        }
        catch (Exception e){
            throw new AccountServiceConfigurationException("Finding account(s) failed due to internal service error.");
        }
    }

    @Override
    public Account debit(long id, long amount)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException,
            InsufficientHoldingException {
        if(id < 0 || amount <= 0)
            throw new AccountInputParameterException("Debiting account failed due to invalid input(s).");

        try {
            Account account = em.find(AccountDB.class, id);
            if (account == null)
                throw new AccountEntityNotFoundException("Could not found any account with provided id.");

            em.getTransaction().begin();
            // Create a transaction in db
            TransactionDB transaction = new TransactionDB();
            transaction.setType("DEBIT");
            transaction.setAmount(amount);
            transaction.setCreated(new Date().toString());
            transaction.setAccount(account);
            account.getTransactions().add(transaction);

            if(account.getHoldings() >= amount){
                transaction.setStatus("OK");
                // Update existing account in db
                account.setHoldings(account.getHoldings() - amount);
                em.persist(transaction);
                em.getTransaction().commit();
                return account;
            }
            else {
                transaction.setStatus("FAILED");
                em.persist(transaction);
                em.getTransaction().commit();
                throw new InsufficientHoldingException("Insufficient holding");
            }
        }
        catch(AccountEntityNotFoundException e){
            throw e;
        }
        catch (InsufficientHoldingException e){
            throw e;
        }
        catch (Exception e){
            throw new AccountServiceConfigurationException("Debiting account failed due to internal service error!");
        }
    }

    @Override
    public Account credit(long id, long amount)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException{
        if(id < 0 || amount <= 0)
            throw new AccountInputParameterException("Crediting account failed due to invalid input(s).");
        try {
            Account account = em.find(AccountDB.class, id);
            if (account == null)
                throw new AccountEntityNotFoundException("Could not found any account with provided id.");

            final TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();
            // Create a transaction in db
            TransactionDB transaction = transactionEntityFacade.create("CREDIT", amount, new Date().toString(), "OK", id);

            // Update existing account in db
            em.getTransaction().begin();
            account.getTransactions().add(transaction);
            account.setHoldings(account.getHoldings() + amount);
            em.persist(account);
            em.getTransaction().commit();

            return account;
        }
        catch(AccountEntityNotFoundException e){
            throw e;
        }
        catch (Exception e){
            throw new AccountServiceConfigurationException("Crediting account failed due to internal service error!");
        }

    }
}


