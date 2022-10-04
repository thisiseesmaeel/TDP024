#[macro_use]
extern crate actix_web;

use{
    actix_web::{middleware, App, HttpServer},
    std::{env, io},
};

mod bank;
mod bank_controller;
mod util;

#[actix_rt::main]
async fn main() -> io::Result<()> {

    //env::set_var("RUST_BACKTRACE","full");
    env::set_var("RUST_LOG", "actix_web=debug, actix_server=info");
    env_logger::init();

    HttpServer::new(|| {
        App::new()
        .wrap(middleware::Logger::default())
        .service(bank_controller::list)
        .service(bank_controller::find_name)
        .service(bank_controller::find_key)
    })
    .bind("127.0.0.1:8070")?
    .run()
    .await
}