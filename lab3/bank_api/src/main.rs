// src/main.rs
//use core::ptr::null;
use actix_web::{App, HttpResponse, HttpServer, Responder, get};
use listenfd::ListenFd;
use std::collections::HashMap;


fn get_db() ->  HashMap<String, String> {
    let mut bank_info: HashMap<String, String> = HashMap::new();

    bank_info.insert("1".to_string(), "SWEDBANK".to_string());
    bank_info.insert("2".to_string(), "IKANOBANKEN".to_string());
    bank_info.insert("3".to_string(), "JPMORGAN".to_string());
    bank_info.insert("4".to_string(), "NORDEA".to_string());
    bank_info.insert("5".to_string(), "CIT".to_string());
    bank_info.insert("6".to_string(), "HANDELSBANKEN".to_string());
    bank_info.insert("7".to_string(), "SBAB".to_string());
    bank_info.insert("8".to_string(), "HSBC".to_string());
    bank_info.insert("9".to_string(), "NORDNET".to_string());

    return bank_info;
}

fn find_bank_by_key(key : &String) -> String {

    let db = get_db();
    let res: String = "".to_string();


        for (k , value) in &db {
            if k == key{
                let res = value;
                return res.to_string();
            }
        }
    return res;

}


fn find_bank_by_name(name : String) -> String {
    let db = get_db();
    let res : String = "".to_string();

    for (k , value) in db {
        if value == name{
            let res = k;
            return res;
        }
    }
    return res;

}

#[get("/bank/ismail")]
async fn index() -> HttpResponse {
    HttpResponse::Ok().body("data")
}

#[get("/bank/list")]
async fn get_all_banks() -> impl Responder { 
    let banks = get_db();
    HttpResponse::Ok().json(banks)
}


#[get("/bank/find.name/")]
async fn find_by_name(name : String) -> impl Responder {
    let db = get_db();
    let na = Vec::from_iter(db.values());

    println!("Name is {:?}", name);
    let result = find_bank_by_name(na[0].to_string());
    HttpResponse::Ok().json(result)
}

#[get("/bank/find.key/")]
async fn find_by_key(key: String) -> impl Responder {

    let db= get_db();
    let ka = Vec::from_iter(db.keys());
/*    for x in 0..ka.len() {
        println!("{} {}", x, ka[x]);
        println!("Key is {:?}", x);
    }*/

    //let res: String = "".to_string();
   /* let ke = Vec::from_iter(db.keys())[0];
    let na = Vec::from_iter(db.values())[0];
*/

    println!("Key is {:?}", key);
    let result = find_bank_by_key(&ka[0].to_string());
    HttpResponse::Ok().json(result)

    //HttpResponse::NotFound().json("NULL")
}


#[actix_rt::main]
async fn main() -> std::io::Result<()> {


    let mut listenfd = ListenFd::from_env();
    let mut server = HttpServer::new(||
        App::new()
            .service(get_all_banks)
            .service(find_by_name)
            .service(find_by_key)
            .service(index)
    );

    server = match listenfd.take_tcp_listener(0)? {
        Some(listener) => server.listen(listener)?,
        None => server.bind("127.0.0.1:8070")?,
    };


    server.run().await
}