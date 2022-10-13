package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountEntityFacadeDB implements AccountEntityFacade {
    private final EntityManager em = EMF.getEntityManager();
    @Override
    public boolean create(String personKey, String bankKey, String accountType) {

        em.getTransaction().begin();

        AccountDB accountDB = new AccountDB();
        accountDB.setPersonKey(personKey);
        accountDB.setAccountType(accountType);
        accountDB.setBankKey(bankKey);
        accountDB.setHoldings(0); // An empty account with zero holdings
        accountDB.setTransactions(new ArrayList<>());

        em.persist(accountDB);
        em.getTransaction().commit();

        return true;
    }

    @Override
    public List<Account> find(String personKey) {
        em.clear();
        Query query = em.createQuery(
                "SELECT account FROM AccountDB account WHERE account.personKey = :personKey"
        );

        query.setParameter("personKey", personKey);

        return (List<Account>) query.getResultList();

    }

    @Override
    public boolean credit(long id, long amount) {
        try {
            EntityManager em = EMF.getEntityManager();
            em.getTransaction().begin();
            Account account = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);

            // Create a transaction in db
            TransactionDB transaction = new TransactionDB();
            transaction.setType("CREDIT");
            transaction.setAmount(amount);
            transaction.setCreated(new Date().toString());
            transaction.setAccount(account);
            account.getTransactions().add(transaction);

            if(amount > 0){
                transaction.setStatus("OK");
                // Update existing account in db
                account.setHoldings(account.getHoldings() + amount);
                em.persist(account);
                em.persist(transaction);
                em.getTransaction().commit();
                em.close();
                return true;
            }
            else {
                transaction.setStatus("FAILED");
                em.persist(transaction);
                em.getTransaction().commit();
                em.close();
                return false;
            }
        }catch (Exception e){
            em.close();
            return false;
        }

    }

    @Override
    public boolean debit(long id, long amount) {
        try{
            EntityManager em = EMF.getEntityManager();
            em.getTransaction().begin();
            Account account = em.find(AccountDB.class, id, LockModeType.PESSIMISTIC_WRITE);

            // Create a transaction in db
            TransactionDB transaction = new TransactionDB();
            transaction.setType("DEBIT");
            transaction.setAmount(amount);
            transaction.setCreated(new Date().toString());
            transaction.setAccount(account);
            account.getTransactions().add(transaction);


            if(account.getHoldings() >= amount && amount > 0){
                transaction.setStatus("OK");
                // Update existing account in db
                account.setHoldings(account.getHoldings() - amount);
                em.persist(account);
                em.persist(transaction);
                em.getTransaction().commit();
                em.close();
                return true;
            }
            else {
                transaction.setStatus("FAILED");
                em.persist(transaction);
                em.getTransaction().commit();
                em.close();
                return false;
            }
        }
        catch (Exception e){
            em.close();
            return false;
        }
    }
}


