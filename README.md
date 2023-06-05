# Distributed Systems Group2

## Create Topic CLI
`kafka-topics.sh --create --bootstrap-server localhost:9092 --topic Roosevelt --partitions 3 --replication-factor 1`
## Consumer CLI
`kafka-console-consumer.sh  --topic Roosevelt --from-beginning --bootstrap-server localhost:9092 --property print.key=true `
## Python Package
`pip install confluent-kafka`
