package se.liu.ida.tdp024.account.data.impl.db.facade;

import org.eclipse.persistence.annotations.OptimisticLocking;
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

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountEntityFacadeDB implements AccountEntityFacade {
    private final EntityManager em = EMF.getEntityManager();
    private final TransactionEntityFacade transactionEntityFacade = new TransactionEntityFacadeDB();

    @Override
    public long create(String personKey, String bankKey, String accountType)
            throws AccountInputParameterException, AccountServiceConfigurationException {
        long id;
        try {
            if (personKey.isEmpty() || bankKey.isEmpty() || accountType.isEmpty()) {
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
            id = accountDB.getId();
        } catch (AccountInputParameterException e) {
            throw e;
        } catch (Exception e) {
            throw new AccountServiceConfigurationException("Creating account failed due to internal service error.");
        }
        return id;
    }

    @Override
    public List<Account> find(String personKey)
            throws AccountEntityNotFoundException, AccountInputParameterException, AccountServiceConfigurationException{
        try {
            em.clear();
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
        catch (AccountInputParameterException | AccountEntityNotFoundException e){
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
            EntityManager em = EMF.getEntityManager();
            em.getTransaction().begin();
            Account account = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);
            if (account == null)
                throw new EntityNotFoundException();

            if(account.getHoldings() >= amount){
                TransactionDB transaction = transactionEntityFacade.create("DEBIT", amount, new Date().toString(), "OK", id);
                // Update existing account in db
                account.getTransactions().add(transaction);
                account.setHoldings(account.getHoldings() - amount);
                em.persist(account);
                //em.persist(transaction);
                em.getTransaction().commit();
                em.close();
                return account;
            }
            else {
                TransactionDB transaction = transactionEntityFacade.create("DEBIT", amount, new Date().toString(), "FAILED", id);
                account.getTransactions().add(transaction);
                em.persist(account);
                //em.persist(transaction);
                em.getTransaction().commit();
                em.close();
                throw new InsufficientHoldingException("Insufficient holding");
            }
        }
        catch (RollbackException e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            // Debiting account failed due to internal service error!
            throw new AccountServiceConfigurationException(e.getMessage());
        }
        catch (EntityNotFoundException e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new AccountEntityNotFoundException("Could not found any account with provided id.");
        }
        catch(InsufficientHoldingException e){
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
            EntityManager em = EMF.getEntityManager();
            em.getTransaction().begin();
            Account account = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);
            if (account == null)
                throw new EntityNotFoundException();

            // Create a transaction in db
            TransactionDB transaction = transactionEntityFacade.create("CREDIT", amount, new Date().toString(), "OK", id);

            // Update existing account in db
            account.getTransactions().add(transaction);
            account.setHoldings(account.getHoldings() + amount);
            em.persist(account);
            //em.persist(transaction);
            em.getTransaction().commit();
            return account;
        }
        catch (RollbackException e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            // "Crediting account failed due to internal service error!"
            throw new AccountServiceConfigurationException(e.getMessage());
        }
        catch (EntityNotFoundException e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }

            throw new AccountEntityNotFoundException("Could not found any account with provided id.");
        }
        catch (Exception e){
            if(em.getTransaction().isActive()){
                em.getTransaction().rollback();
            }
            throw new AccountServiceConfigurationException("Crediting account failed due to internal service error!");
        }
//        finally {
////            em.getTransaction().begin();
////            TransactionDB transaction = transactionEntityFacade.create("CREDIT", amount, new Date().toString(), "FAILED", id);
////            em.persist(transaction);
////            em.getTransaction().commit();
//            em.close();
//        }

    }
}


