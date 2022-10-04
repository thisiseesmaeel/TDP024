use {
    serde::{Deserialize, Serialize},
};


// JSON (REST)
 
#[derive(Debug, Deserialize, Serialize)]
pub struct Bank {
    pub key: i32,
    pub name: String,
}



// Database 
#[derive(Debug, Deserialize, Serialize)]
pub struct Database {
    pub key: i32,
    pub name: String,
}