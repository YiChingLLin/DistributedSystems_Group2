# Apache Kafka應用 - 公車即時擁擠度

### 分散式系統 Group2

107703049, [108207329](https://github.com/xoxonut), [109703003](https://github.com/Tanlikfeng), 109703032, [110356019](https://github.com/YiChingLLin), [110356022](https://github.com/dabaoku), 110356046, [111356023](https://github.com/106306067)

## 應用介紹
Kafka作為一個事件串流平台，主要用於處理大量的串流事件資料的分散式系統。在本次期末專題中，我們將Kafka應用於計算公車即時擁擠度，用於處理大量即時的公車上下車人數資料以估計公車上目前人數是否過於擁擠，藉此提供民眾作為是否轉乘其他班次、路線之參考依據。本應用假設所有公車僅接受電子票證方式搭乘且民眾於上下車皆會感應票證並即時回傳。在程式中以隨機產生數值模擬公車即時的上下車人數作為資料來源，再透過計算將目前公車上人數的資料傳出，並即時呈現於儀表板中。

## 設計概念
- producer: 每次到站時發出event
    - {車號, 上車人數, 下車人數}
    - {車號, 目前車上人數}
- topic: 按路線或車號分類
- consumer: 計算出目前公車上有幾個人 
    - 目前該輛公車人數 = 前站該輛公車人數 - 下車人數 + 上車人數

## 檔案說明
以下為各資料夾中重要檔案用途說明
#### Raw Data
- raw_data_creator.py: 作為producer發出event來模擬各輛公車即時的上下車人數資料
- sender.py: 呼叫raw_data_creator以啟動多個process

#### Bus
src/main/java
- calculator.java: 接收各輛公車即時的上下車人數資料，以持續計算目前車上人數並更新
- Initialize.java: 將各輛公車的初始人數資料傳進topic
- test.java: 測試用

Bus_server
- kafka-consumer.js: 取得各輛公車目前車上人數

#### Dashboard
- index.html: 將各輛公車的即時擁擠度(車上人數)以前端視覺化畫面呈現

## Requirement
- Kafka
- Zookeeper
- Java
- Python
- Node.js
- confluent-kafka `pip install confluent-kafka`

## Flow
1. Start **Zookeeper**
    - Windows: 

    `.\zookeeper-server-start.bat ..\..\config\zookeeper.properties`

    - Mac: 

    `zookeeper-server-start /opt/homebrew/etc/kafka/zookeeper.properties`

2. Start **Kafka**
    - Windows:

    `.\kafka-server-start.bat ..\..\config\server.properties`

    - Mac: 

    `kafka-server-start /opt/homebrew/etc/kafka/server.properties`

3. Create **Topic**
    - Windows: 

    `.\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic Roosevelt --partitions 3 --replication-factor 1`

    `.\kafka-topics.bat --create --bootstrap-server localhost:9092 --topic crowdedness --partitions 3 --replication-factor 1`

    - Mac: 

    `kafka-topics --create --bootstrap-server localhost:9092 --topic Roosevelt --partitions 3 --replication-factor 1`

    `kafka-topics --create --bootstrap-server localhost:9092 --topic crowdedness --partitions 3 --replication-factor 1`

    Consumer
    - Windows: 
    
    `kafka-console-consumer.sh  --topic Roosevelt --from-beginning --bootstrap-server localhost:9092 --property print.key=true`

    - Mac: 
    
    `kafka-console-consumer  --topic Roosevelt --from-beginning --bootstrap-server localhost:9092 --property print.key=true`

4. Run Bus/src/main/java/**Initialize.java** (第一次執行時需初始人數)

5. Run Bus/src/main/java/**calculator.java**

6. Run Bus/Bus_server/**kafka-consumer.js**

    `node kafka-consumer.js`

7. Run raw-data/**sender.py**

    `python sender.py`

8. Open dashboard/**index.html**

![image](https://github.com/YiChingLLin/DistributedSystems_Group2/blob/readme/img/screencapture-dashboard.png)