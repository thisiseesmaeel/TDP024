use actix_web::{App, HttpResponse, HttpServer, Responder, get};
use listenfd::ListenFd;
use serde::Deserialize;
use actix_web::web::Query;
use chrono::prelude::*;

mod database;
mod kafka;


#[get("/bank/list")]
async fn get_all_banks() -> impl Responder { 
    let banks = database::get_db();
    let log : String = "Listing all banks at ".to_owned();
    let date: String = Utc::now().to_string();
    let log: String = format!("{} \"{}\"", log, date);
    kafka::get_producer(log);

    HttpResponse::Ok().json(banks)
}


#[derive(Deserialize)]
struct Name {
    name: String
}


#[get("/bank/find.name")]
async fn find_by_name(param : Query<Name>) -> impl Responder {
    let name: String = (*param.name).to_string();
    let result  = database::find_bank_by_name(name);
    let name: String = (*param.name).to_string();
    let log : String = format!("Finding bank with name {} at", name).to_owned();
    let date: String = Utc::now().to_string();
    let log: String = format!("{} \"{}\"", log, date);
    kafka::get_producer(log);
    if result.is_empty() {
        let not_found = database::give_none();
        HttpResponse::Ok().json(not_found)
    } else{
        HttpResponse::Ok().body(result)
    }
}

#[derive(Deserialize)]
struct Key {
    key: String
}

#[get("/bank/find.key")]
async fn find_by_key(param : Query<Key>) -> impl Responder {
    let key: String = (*param.key).to_string();
    let result: String = database::find_bank_by_key(key);
    let key: String = (*param.key).to_string();
    let log : String = format!("Finding bank with key {} at", key).to_owned();
    let date: String = Utc::now().to_string();
    let log: String = format!("{} \"{}\"", log, date);
    kafka::get_producer(log);
    
    if result.is_empty() {
        let not_found = database::give_none();
        HttpResponse::Ok().json(not_found)
    } else{
        HttpResponse::Ok().body(result)
    }
}



#[actix_rt::main]
async fn main() -> std::io::Result<()> {
    let mut listenfd = ListenFd::from_env();
    let mut server = HttpServer::new(||
        App::new()
            .service(get_all_banks)
            .service(find_by_name)
            .service(find_by_key)
    );

    server = match listenfd.take_tcp_listener(0)? {
        Some(listener) => server.listen(listener)?,
        None => server.bind("127.0.0.1:8070")?,
    };


    server.run().await
}

// systemfd --no-pid -s http::8070 -- cargo watch -x run