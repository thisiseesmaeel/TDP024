package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
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
        accountDB.setTransactions(new ArrayList<Transaction>());

        em.persist(accountDB);
        em.getTransaction().commit();

        return true;
    }

    @Override
    public List<Account> find(String personKey) {

        Query query = em.createQuery(
                "SELECT account FROM AccountDB account WHERE account.personKey = :personKey"
        );

        query.setParameter("personKey", personKey);

        return (List<Account>) query.getResultList();
    }

    @Override
    public boolean credit(long id, long amount) {

        Account account = em.find(AccountDB.class, id);

        em.getTransaction().begin();
        account.setHoldings(account.getHoldings() + amount);
        em.getTransaction().commit();

        return true;
    }

    @Override
    public boolean debit(long id, long amount) {
        Account account = em.find(AccountDB.class, id);

        em.getTransaction().begin();
        account.setHoldings(account.getHoldings() - amount);
        em.getTransaction().commit();

        return true;
    }
}


