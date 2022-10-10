package se.liu.ida.tdp024.account.data.impl.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;

import javax.persistence.*;
import java.util.List;

@Entity
public class AccountDB implements Account {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // Auto increment the id each time we create an account
    private long id;
    private String personKey;
    private String accountType;
    private String bankKey;
    private long holdings;
    @JsonIgnore
    @OneToMany(mappedBy = "account", targetEntity = TransactionDB.class)
    private List<Transaction> transactions;

    @Override
    public long getId() {
        return id;
    }

//    @Override
//    public void setId(long id) {
//        this.id = id;
//    }

    @Override
    public String getPersonKey() {
        return personKey;
    }

    @Override
    public void setPersonKey(String personKey) {
        this.personKey = personKey;
    }

    @Override
    public String getAccountType() {
        return accountType;
    }

    @Override
    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String getBankKey() {
        return bankKey;
    }

    @Override
    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    @Override
    public long getHoldings() {
        return holdings;
    }

    @Override
    public void setHoldings(long holdings) {
        this.holdings = holdings;
    }

    @Override
    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    @Override
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }


}
