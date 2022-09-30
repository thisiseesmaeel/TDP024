# How to set up environment
-  How to set up Elixir environment
-  How to set up Rust environment
-  How to set up Kafka

### How to set up Kafka
Download **Kafka** from [here](https://downloads.apache.org/kafka/3.2.3/kafka-3.2.3-src.tgz) and fallow the steps below:

- `tar -xzf kafka_2.13-3.2.1.tgz`
- `cd kafka_2.13-3.2.1`
- `bin/zookeeper-server-start.sh config/zookeeper.properties` (starts the zookeper)
- `bin/kafka-server-start.sh config/server.properties` (run this command in another terminal to start the Kafka broker service)
- `bin/kafka-topics.sh --create --topic quickstart-events --bootstrap-server localhost:9092` (creat topic named quickstart-events)
- `bin/kafka-console-producer.sh --topic quickstart-events --bootstrap-server localhost:9092` (If you want to produce logs)
- `bin/kafka-console-consumer.sh --topic quickstart-events --from-beginning --bootstrap-server localhost:9092` (If you want to read logs)
