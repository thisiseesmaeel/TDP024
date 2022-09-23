package se.liu.ida.tdp024.account.logic.mock;

import java.util.HashMap;
import java.util.Map;

public class BankMock {
    static HashMap<String, String> banks = new HashMap<>();

    static {
        banks.put("1", "SWEDBANK");
        banks.put("2", "IKANOBANKEN");
        banks.put("3", "JPMORGAN");
        banks.put("4", "NORDEA");
        banks.put("5", "CITIBANK");
        banks.put("6", "HANDELSBANKEN");
        banks.put("7", "SBAB");
        banks.put("8", "HSBC");
        banks.put("9", "NORDNET");
    }

    public static String findBankById(String id){
        return banks.get(id);
    }

    public static String findBankByName(String name){
        for(Map.Entry<String, String> entry: banks.entrySet()){
            if(entry.getValue().equals(name)){
                return entry.getValue();
            }
        }
        return null;
    }
}
