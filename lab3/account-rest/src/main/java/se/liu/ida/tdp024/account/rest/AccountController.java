package se.liu.ida.tdp024.account.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @RequestMapping("/account-rest/account/create")
    public String create(@RequestParam(value="name", defaultValue="World") String name) {

        return "Create works!";
    }

    @RequestMapping("/account-rest/account/find/person")
    public String findPerson(@RequestParam(value="name", defaultValue="World") String name) {

        return "Find person works!";
    }

    @RequestMapping("/account-rest/account/debit")
    public String debit(@RequestParam(value="name", defaultValue="World") String name) {

        return "Debit works!";
    }

    @RequestMapping("/account-rest/account/credit")
    public String credit(@RequestParam(value="name", defaultValue="World") String name) {

        return "Credit works!";
    }

    @RequestMapping("/account-rest/account/transaction")
    public String transaction(@RequestParam(value="name", defaultValue="World") String name) {

        return "Transaction works!";
    }

}
