# Apache Kafka應用 - 公車即時壅擠度

### Distributed Systems Group2
Members:
107703049, [108207329](https://github.com/xoxonut), 109703003, 109703032
[110356019](https://github.com/YiChingLLin), [110356022](https://github.com/dabaoku), 110356046, [111356023](https://github.com/106306067)

## 研究動機

## 設計概念
- producer: 每次到站時發出event:{車號, 上車人數, 下車人數}
- topic: 按車號分類
- consumer: 計算出目前公車上有幾個人

## Requirement
- confluent-kafka `pip install confluent-kafka`
- java

## Flow
- Create Topic CLI
`kafka-topics.sh --create --bootstrap-server localhost:9092 --topic Roosevelt --partitions 3 --replication-factor 1`
- Consumer CLI
`kafka-console-consumer.sh  --topic Roosevelt --from-beginning --bootstrap-server localhost:9092 --property print.key=true `
- 開啟raw-data/sender.py
- 確認開啟kafka broker
- 執行initialize.java (第一次執行時需初始人數)
- 執行calculator.java
- 開啟Bus/Bus_server/kafka-consumer.js
- 開啟dashboard/index.html


