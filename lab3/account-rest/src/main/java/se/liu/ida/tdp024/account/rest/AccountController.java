package se.liu.ida.tdp024.account.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;


@RestController
@RequestMapping("/account-rest")
public class AccountController {
    AccountLogicFacade accountLogicFacade = new AccountLogicFacadeImpl(new AccountEntityFacadeDB());

    @RequestMapping("/account/create")
    public String create(@RequestParam String accountType, @RequestParam String person, @RequestParam String bank) {
        accountLogicFacade.create(accountType, person, bank);
        return "Create works!";
    }

    @RequestMapping("/account/find/person")
    public String findPerson(@RequestParam String personKey) {
        accountLogicFacade.find(personKey);
        return "Find person works!";
    }

    @RequestMapping("/account/debit")
    public String debit(@RequestParam(value="name", defaultValue="World") String name) {

        return "Debit works!";
    }

    @RequestMapping("/account/credit")
    public String credit(@RequestParam(value="name", defaultValue="World") String name) {

        return "Credit works!";
    }

    @RequestMapping("/account/transaction")
    public String transaction(@RequestParam(value="name", defaultValue="World") String name) {

        return "Transaction works!";
    }

}