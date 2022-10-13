package se.liu.ida.tdp024.account.rest.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.http.HttpStatus;
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
import java.util.concurrent.ExecutionException;

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

    private final AccountLogicFacade accountLogicFacade;
    private final TransactionLogicFacade transactionLogicFacade;

    public AccountService(AccountLogicFacade accountLogicFacade, TransactionLogicFacade transactionLogicFacade) {
        this.accountLogicFacade = accountLogicFacade;
        this.transactionLogicFacade = transactionLogicFacade;

    }

    public ResponseEntity<String> create(String person, String bank, String accounttype) {
        try{
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Trying to create an account " + new Date())).get();

            if (accountLogicFacade.create(person, bank, accounttype)) {
                producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Account created successfully " + new Date())).get();
                return ResponseEntity.ok("Ok");
            }
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to create the account " + new Date()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to create the account");

        }
        catch (AccountInputParameterException e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to create the account " + e.getMessage() + " " + new Date()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (AccountEntityNotFoundException e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to create the account " + e.getMessage() + " " + new Date()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to create the account. Either bank name or person key does not exist");
        }
        catch (AccountServiceConfigurationException e) {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to create the account " + e.getMessage() + " " + new Date()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        catch (Exception e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to create the account " + e.getMessage() + " " + new Date()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public ResponseEntity findPerson(String person) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Finding all accounts of a person " + new Date())).get();
            List<Account> response = accountLogicFacade.find(person);
            return ResponseEntity.ok(response);
        } catch (AccountEntityNotFoundException e) {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java):  Failed to find the person " + new Date()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (AccountInputParameterException e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to find the person " + new Date()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (AccountServiceConfigurationException e) {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to find the person " + new Date()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        catch (Exception e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Something went wrong => " + e.getMessage() + " " + new Date()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity<String> debit(long id, long amount) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Trying to debit from an account " + new Date())).get();
            accountLogicFacade.debit(id, amount);
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Debited from the account successfully " + new Date())).get();
            return ResponseEntity.ok("Ok");
        }
        catch (AccountEntityNotFoundException | AccountInputParameterException
               | AccountServiceConfigurationException | InsufficientHoldingException e){
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to debit from the account " + new Date()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Something went wrong => " + e.getMessage() + " " + new Date()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bank-system (Java): Something went wrong. Cause => " + e.getMessage());
        }

    }

    public ResponseEntity<String> credit(long id, long amount) {
        try {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Trying to credit an account " + new Date())).get();
            accountLogicFacade.credit(id, amount);
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Credited the account successfully " + new Date())).get();

            return ResponseEntity.ok("Ok");

        } catch (AccountEntityNotFoundException e){
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to credit the account " + new Date()));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (AccountInputParameterException e) {
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to credit the account " + new Date()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (AccountServiceConfigurationException e) {
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to credit the account " + new Date()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        catch (Exception e){
            producer.send(new ProducerRecord<>(TRANSACTION_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Failed to credit the account " + new Date()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    public ResponseEntity transaction(long id) {
        try{
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Inquiry for transaction details " + new Date())).get();
            List<Transaction> transactions = transactionLogicFacade.findByAccountId(id);
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Transaction details succeeded " + new Date())).get();
            return ResponseEntity.status(HttpStatus.OK).body(transactions);
        }catch (AccountServiceConfigurationException e){
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Transaction details failed " + new Date()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (ExecutionException | InterruptedException e) {
            producer.send(new ProducerRecord<>(REST_TOPIC, System.currentTimeMillis(), "Bank-system (Java): Transaction details failed " + new Date()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
