package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Transaction extends Serializable {

    long getId();

    void setId(long id);

    String getType();

    void setType(String type);

    long getAmount();

    void setAmount(long amount);
    String getCreated();

    void setCreated(String created);

    String getStatus();

    void setStatus(String status);

    Account getAccount();

    void setAccount(Account account);

}