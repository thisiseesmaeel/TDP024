use kafka::producer::{Producer, Record};

pub fn get_producer(log : String){
    let hosts = vec!["localhost:9092".to_owned()];

 let mut producer =
   Producer::from_hosts(hosts)
     .create()
     .unwrap();

   let buf = format!("{}", log);
   producer.send(&Record::from_value("REST", buf.as_bytes())).unwrap();
 
}