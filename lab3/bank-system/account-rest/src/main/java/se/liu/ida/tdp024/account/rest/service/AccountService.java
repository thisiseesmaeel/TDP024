package se.liu.ida.tdp024.account.rest.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.exception.AccountEntityNotFoundException;
import se.liu.ida.tdp024.account.data.exception.AccountInputParameterException;
import se.liu.ida.tdp024.account.data.exception.AccountServiceConfigurationException;
import se.liu.ida.tdp024.account.data.exception.InsufficientHoldingException;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.api.facade.TransactionLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;
import se.liu.ida.tdp024.account.logic.impl.facade.TransactionLogicFacadeImp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class AccountService {
    private final static String REST_TOPIC = "REST";
    private final static String TRANSACTION_TOPIC = "TRANSACTION";
    private final Producer<Long, String> producer = createProducer();
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

    private final AccountLogicFacade accountLogicFacade; //= new AccountLogicFacadeImpl(new AccountEntityFacadeDB());
    private final TransactionLogicFacade transactionLogicFacade; // = new TransactionLogicFacadeImp(new TransactionEntityFacadeDB());

    public AccountService(AccountLogicFacade accountLogicFacade, TransactionLogicFacade transactionLogicFacade) {
        this.accountLogicFacade = accountLogicFacade;
        this.transactionLogicFacade = transactionLogicFacade;

    }

    public ResponseEntity create(String person, String bank, String accounttype) {
        try{
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Creating user at: \"" + new Date() + "\"")).get();

            if (accountLogicFacade.create(person, bank, accounttype)) {
                producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "User created at: \"" + new Date() + "\"")).get();
                return ResponseEntity.ok("Ok");
            }
        } catch (AccountServiceConfigurationException | AccountInputParameterException e) {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Could not create user: " + e.getMessage()));
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Could not create user: " + e.getMessage()));
            return ResponseEntity.status(400).body(e.getMessage());
        }
        return null;
    }

    public ResponseEntity findPerson(String person) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Finding all accounts of a person: \"" + new Date() + "\"")).get();
            List<Account> response = accountLogicFacade.find(person);
            return ResponseEntity.ok(response);
        } catch (AccountEntityNotFoundException | AccountInputParameterException |
                 AccountServiceConfigurationException e) {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Could not find the person"));
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Something went wrong."));
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    public ResponseEntity debit(long id, long amount) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Trying to debit from an account at: \"" + new Date() + "\"")).get();
            accountLogicFacade.debit(id, amount);
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Debited from account at: \"" + new Date() + "\"")).get();
            return ResponseEntity.ok("Ok");
        }
        catch (AccountEntityNotFoundException | AccountInputParameterException
               | AccountServiceConfigurationException | InsufficientHoldingException e){
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Could not debit from account at: \"" + new Date() + "\""));
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(400).body("Something went wrong. Cause => " + e.getMessage());
        }

    }

    public ResponseEntity credit(long id, long amount) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Trying to credit an account at: \"" + new Date() + "\"")).get();
            accountLogicFacade.credit(id, amount);
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Credited from account at: \"" + new Date() + "\"")).get();

            return ResponseEntity.ok("Ok");


        } catch (AccountEntityNotFoundException | AccountInputParameterException | AccountServiceConfigurationException e) {
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Could not credit account at: \"" + new Date() + "\""));
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception e){
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Could not credit account at: \"" + new Date() + "\""));
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    public List<Transaction> transaction(long id) {
        try{
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Transaction details at: \"" + new Date() + "\"")).get();
            return transactionLogicFacade.findByAccountId(id);

        }catch (Exception e){
            System.out.println(e);
            return new ArrayList<>();
        }
    }
}
