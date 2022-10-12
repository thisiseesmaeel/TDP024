### How to set up Elixir environment (person api)
Make sure you have elixir installed by running the `elixir -v`. Check [here](https://hexdocs.pm/phoenix/installation.html) for installation. Then follow the steps below:
- Open `person-api` directory
- Run `mix deps.get` in order to get projects dependencies
- Run `mix phx.server` in order to run the application
- Aplication should run on port **http://localhost:8060/person**

### How to set up Rust environment (bank api)
- Open **"bank_api"** directory
- Run `systemfd --no-pid -s http::8070 -- cargo watch -x run`
- Aplication should run on port **http://localhost:8070/bank**

### How to set up Kafka
Download **Kafka** from [here](https://downloads.apache.org/kafka/3.2.3/kafka-3.2.3-src.tgz) and fallow the steps below:

- `tar -xzf kafka_2.13-3.2.1.tgz`
- `cd kafka_2.13-3.2.1`
- `bin/zookeeper-server-start.sh config/zookeeper.properties` (starts the zookeper)
- `bin/kafka-server-start.sh config/server.properties` (run this command in another terminal to start the Kafka broker service)

- Create two topics named **"REST"** and **"Transaction"**:
    - `bin/kafka-topics.sh --create --topic REST --bootstrap-server localhost:9092` (creat topic named REST)  
    - `bin/kafka-topics.sh --create --topic TRANSACTION --bootstrap-server localhost:9092` (creat topic named TRANSACTION)
- `bin/kafka-console-producer.sh --topic <TOPIC NAME> --bootstrap-server localhost:9092` (If you want to produce logs)
- `bin/kafka-console-consumer.sh --topic <TOPIC NAME> --from-beginning --bootstrap-server localhost:9092` (If you want to read logs)


### How to run bank-system application
- Open **"bank-system"** directory
- Run `mvn clean install`
- Open **"account-rest"**
- Run `mvn spring-boot:run`