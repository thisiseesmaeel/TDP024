use std::collections::HashMap;

pub fn get_db() ->  HashMap<String, String> {
    let mut bank_info: HashMap<String, String> = HashMap::new();

    bank_info.insert("1".to_string(), "SWEDBANK".to_string());
    bank_info.insert("2".to_string(), "IKANOBANKEN".to_string());
    bank_info.insert("3".to_string(), "JPMORGAN".to_string());
    bank_info.insert("4".to_string(), "NORDEA".to_string());
    bank_info.insert("5".to_string(), "CITIBANK".to_string());
    bank_info.insert("6".to_string(), "HANDELSBANKEN".to_string());
    bank_info.insert("7".to_string(), "SBAB".to_string());
    bank_info.insert("8".to_string(), "HSBC".to_string());
    bank_info.insert("9".to_string(), "NORDNET".to_string());

    return bank_info;
}

pub fn find_bank_by_key(key : String) -> String{
    let db = get_db();
    let res : String = "".to_string();  

    for (k , v) in db {
        //println!("key {} and param key is {}", k, key);
        if k == key{ 
            let res = v;
            return res;
        } 
    }
    return res;
}

pub fn find_bank_by_name(name : String) -> String{
    let db = get_db();
    let res : String = "".to_string();  

    for (k , v) in db {
        if v == name{ 
            let res = k;
            return res;
        } 
    }
    return res;
}

pub fn give_none() -> Option<String>{
    return None;
}

