use {
    actix_web::HttpResponse,
    //actix_web::web::Json,

    crate::bank::*,
    crate::util::*,
};


// List all banks

#[get("bank/list")]
pub async fn list() -> HttpResponse {


    let banks: Vec<Bank> = vec![]; 

    ResponseType::Ok(banks).get_response()
}

// Find bank by name
#[get("bank/find.name")]
pub async fn find_name() -> HttpResponse {

    let bank: Option<Bank> = None;
    match bank {
        Some(bank) => ResponseType::Ok(bank).get_response(),
        None => ResponseType::NotFound(
            NotFoundMessage::new("Bank Name not found.".to_string())
        ).get_response(),
    }
}

// Find bank by key
#[get("bank/find.key")]
pub async fn find_key() -> HttpResponse {

    let bank: Option<Bank> = None;
    match bank {
        Some(bank) => ResponseType::Ok(bank).get_response(),
        None => ResponseType::NotFound(
            NotFoundMessage::new("Bank Key not found.".to_string())
        ).get_response(),
    }
}