package se.liu.ida.tdp024.account.data.api.entity;

import javax.persistence.Entity;
import java.io.Serializable;
import java.util.List;

public interface Account extends Serializable {

    long getId();
    //void setId(long id);

    String getPersonKey();
    void setPersonKey(String personKey);

    String getAccountType();
    void setAccountType(String accountType);

    String getBankKey();
    void setBankKey(String bankKey);

    long getHoldings();
    void setHoldings(long holdings);

    List<Transaction> getTransactions();

    void setTransactions(List<Transaction> transactions);

}
