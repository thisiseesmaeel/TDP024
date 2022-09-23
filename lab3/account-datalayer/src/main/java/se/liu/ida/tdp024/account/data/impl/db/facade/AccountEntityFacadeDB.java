package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class AccountEntityFacadeDB implements AccountEntityFacade {
    private EntityManager em = EMF.getEntityManager();
    @Override
    public boolean create(String accountType, String personKey, String bankKey) {

        em.getTransaction().begin();

        AccountDB accountDB = new AccountDB();
        accountDB.setId(2); // How to find a unique id?
        accountDB.setPersonKey(personKey);
        accountDB.setAccountType(accountType);
        accountDB.setBankKey(bankKey);
        accountDB.setHoldings(0); // An empty account with zero holdings
        em.persist(accountDB);

        em.getTransaction().commit();

        return true;
    }

    @Override
    public List<Account> find(String personKey) {

        Query query = em.createQuery(
                "SELECT * FROM AccountDB WHERE personKey = :personKey"
        );

        query.setParameter("personKey", personKey);

        return (List<Account>) query.getResultList();
    }

    @Override
    public boolean credit(long id) {
        return false;
    }

    @Override
    public boolean debit(long id) {
        return false;
    }
}


