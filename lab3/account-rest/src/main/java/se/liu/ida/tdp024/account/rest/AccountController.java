package se.liu.ida.tdp024.account.rest;

import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;

import java.util.List;


@RestController
@RequestMapping("/account-rest")
public class AccountController {
    AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());

    @RequestMapping("/account/create")
    public String create(@RequestParam(value = "person", defaultValue = "") String person,
                         @RequestParam(value = "bank", defaultValue = "") String bank,
                         @RequestParam(value = "accounttype", defaultValue = "")String accounttype) {

        if (person.isEmpty() || bank.isEmpty() || accounttype.isEmpty())
            return "FAILED";
        if (accountLogicFacade.create(person, bank, accounttype))
            return "OK";

        return "FAILED";
    }

    @RequestMapping("/account/find/person")
    public List<Account> findPerson(@RequestParam String person) {
        return accountLogicFacade.find(person);
    }

    @RequestMapping("/account/debit")
    public String debit(@RequestParam long id, @RequestParam long amount) {
        if(accountLogicFacade.debit(id, amount))
            return "OK";
        return "FAILED";
    }

    @RequestMapping("/account/credit")
    public String credit(@RequestParam long id, @RequestParam long amount) {
        if(accountLogicFacade.credit(id, amount))
            return "OK";
        return "FAILED";
    }

    @RequestMapping("/account/transaction")
    public String transaction(@RequestParam long id) {

        return "Transaction works!";
    }

//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    public void handleMissingParams(MissingServletRequestParameterException ex) {
//        String missingParam = ex.getParameterName();
//        System.out.println(missingParam + " parameter is missing");
//    }

}