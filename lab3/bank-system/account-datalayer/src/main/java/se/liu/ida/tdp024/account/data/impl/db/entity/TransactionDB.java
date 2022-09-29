package se.liu.ida.tdp024.account.data.impl.db.entity;

//import org.codehaus.jackson.annotate.JsonIgnore;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class TransactionDB implements Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // Auto increment the id each time we create a transaction
    private long id;
    private String type;
    private long amount;
    private String created;
    private String status;

    @JsonIgnore
    @ManyToOne(targetEntity = AccountDB.class)
    private Account account;

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String getCreated() {
        return this.created;
    }

    @Override
    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Account getAccount() {
        return this.account;
    }

    @Override
    public void setAccount(Account account) {
        this.account = account;
    }
}
