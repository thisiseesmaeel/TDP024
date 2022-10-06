package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.util.EMF;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {
    private final EntityManager em = EMF.getEntityManager();
    @Override
    public TransactionDB create(String type, long amount, String created, String status, long accountId) {

        em.getTransaction().begin();

        TransactionDB transaction = new TransactionDB();
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setCreated(created);
        transaction.setStatus(status);
        transaction.setCreated(created);
        transaction.setAccount(em.find(AccountDB.class, accountId)); // Find belonging account and relate this transaction to it
                                                                   // DB handles the rest. It creates two tables and add account id as
                                                                   // a column into the transaction table. (one -> many)

        em.persist(transaction);
        em.getTransaction().commit();

        return transaction;
    }

    @Override
    public List<Transaction> findByAccountId(long accountID) {
        em.clear(); // If we do not clear entity manager it will show old data of account
        Query query = em.createQuery(
                 "SELECT transaction FROM TransactionDB transaction WHERE transaction.account.id = :accountID");

        query.setParameter("accountID", accountID);

        return (List<Transaction>) query.getResultList();
    }
}
