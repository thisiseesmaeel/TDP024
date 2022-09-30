package se.liu.ida.tdp024.account.rest;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImp;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@RestController
@RequestMapping("/account-rest")
public class AccountController {

    private final static String REST_TOPIC = "REST";
    private final static String TRANSACTION_TOPIC = "TRANSACTION";
    private final Producer <Long, String> producer = createProducer();
    private final static String BOOTSTRAP_SERVERS = "https://localhost:9092";
    private static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    private final AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
    private final TransactionLogicFacade transactionLogicFacade = new TransactionLogicFacadeImp(new TransactionEntityFacadeDB());
    @RequestMapping("/account/create")
    public String create(@RequestParam(value = "person", defaultValue = "") String person,
                         @RequestParam(value = "bank", defaultValue = "") String bank,
                         @RequestParam(value = "accounttype", defaultValue = "")String accounttype) {
        try{
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Creating user at: \"" + new Date() + "\"")).get();

            if (person.isEmpty() || bank.isEmpty() || accounttype.isEmpty()){
                producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Could not create user: \"" + new Date() + "\"")).get();
                return "FAILED";
            }
            if (accountLogicFacade.create(person, bank, accounttype)) {
                producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "User created at: \"" + new Date() + "\"")).get();
                return "OK";
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
        // Do we need a log here?
        return "FAILED";
    }


    @RequestMapping("/account/find/person")
    public List<Account> findPerson(@RequestParam String person) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Finding all accounts of a person: \"" + new Date() + "\"")).get();
        }catch (Exception e){
            System.out.println(e);
        }
        return accountLogicFacade.find(person);
    }

    @RequestMapping("/account/debit")
    public String debit(@RequestParam long id, @RequestParam long amount) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Trying to debit from an account at: \"" + new Date() + "\"")).get();
            if(accountLogicFacade.debit(id, amount)){
                producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Debited from account at: \"" + new Date() + "\"")).get();
                return "OK";
            }
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Could not debit from account at: \"" + new Date() + "\"")).get();
            return "FAILED";
        }
        catch (Exception e){
            System.out.println(e);
            return "FAILED";
        }
    }

    @RequestMapping("/account/credit")
    public String credit(@RequestParam long id, @RequestParam long amount) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Trying to credit an account at: \"" + new Date() + "\"")).get();
            if(accountLogicFacade.credit(id, amount)) {
                producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Credited from account at: \"" + new Date() + "\"")).get();
                return "OK";
            }
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Could not credit account at: \"" + new Date() + "\"")).get();
            return "FAILED";

        }
        catch (Exception e){
                System.out.println(e);
                return "FAILED";
            }
    }

    @RequestMapping("/account/transactions")
    public List<Transaction> transaction(@RequestParam long id) {
        try{
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Transaction details at: \"" + new Date() + "\"")).get();
            return transactionLogicFacade.findByAccountId(id);

        }catch (Exception e){
            System.out.println(e);
            return new ArrayList<>();
        }
    }

}