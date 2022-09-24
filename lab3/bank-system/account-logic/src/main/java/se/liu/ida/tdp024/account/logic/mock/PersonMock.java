package se.liu.ida.tdp024.account.logic.mock;

import java.util.HashMap;
import java.util.Map;

public class PersonMock {
    static HashMap<String, String> persons = new HashMap<>();

    static  {
        persons.put("1", "Jakob Pogulis");
        persons.put("2", "Xena");
        persons.put("3", "Marcus Bendtsen");
        persons.put("4", "Zorro");
        persons.put("5", "Q");

    }

    public static String findPersonById(String key){
        return persons.get(key);
    }

    public static boolean findPersonByName(String name){
        for(Map.Entry<String, String> entry: persons.entrySet()){
            if(entry.getValue().equals(name)){
                return true;
            }
        }
        return false;
    }
}
