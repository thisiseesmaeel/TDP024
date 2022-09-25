package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import java.util.Date;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {
    private final EntityManager em = EMF.getEntityManager();

    @Override
    public boolean create(String type, long amount, String status, long accountId) {
        em.getTransaction().begin();

        Transaction transaction = new TransactionDB();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setStatus(status);
        transaction.setCreated(new Date().toString());
        transaction.setAccount(em.find(Account.class, accountId)); // Find belonging account and relate this transaction to it
                                                                   // DB handles the rest. It creates two tables and add account id as
                                                                   // a column into the transaction table. (one -> many)

        em.persist(transaction);
        em.getTransaction().commit();

        return true;
    }
}
