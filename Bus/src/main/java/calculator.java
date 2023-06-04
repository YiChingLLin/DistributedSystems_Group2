import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

import io.confluent.kafka.serializers.KafkaJsonSerializer;
import io.confluent.kafka.serializers.KafkaJsonDeserializer;

import static io.confluent.kafka.schemaregistry.client.rest.Versions.JSON;

public class calculator {
    static HashMap<String, Long> currentNumber = new HashMap<String, Long>();
    static KafkaConsumer<String, Bus> consumerFromBus;
    static KafkaConsumer<String, BusCurrent> consumerFromCurrent;
    static Producer<String, BusCurrent> producer;
    static ArrayList<String> carNumberList = new ArrayList<String>();
    static Gson gson= new Gson();

    public static void main(String[] args) throws Exception {

        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off"); //"off", "trace", "debug", "info", "warn", "error".
        InitConsumer();
        InitProducer();
        Logger logger = LoggerFactory.getLogger(calculator.class);
        carNumberList.add("NCCU-001");
        carNumberList.add("NCCU-002");
        carNumberList.add("NCCU-003");
        producer.initTransactions();


        //consume from bus
        while (true) {
            ConsumerRecords<String, Bus> records = consumerFromBus.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, Bus> record : records) {
                logger.info(String.valueOf(record.value()));
                String busStr = new Gson().toJson(record.value(), Map.class);
                System.out.println(busStr);
                Bus busRecord = toBus(busStr);


                producer.beginTransaction();        //start atomically transaction
                try {
                    PollFromCurrent(busRecord);
                    Process(busRecord);
                    consumerFromBus.commitSync();
                    producer.commitTransaction();
                    System.out.println("Bus " + busRecord.getCarNumber() + " now have " + currentNumber.get(busRecord.getCarNumber()) + " people.");

                } catch ( Exception e ) {
                    producer.abortTransaction();    //end atomically transaction
                }
            }
        }
    }

    private static void InitConsumer() {
        //consumer consume from bus
        Properties propsConsumerTx = new Properties();
        propsConsumerTx.put("bootstrap.servers", "localhost:9092");
        propsConsumerTx.put("group.id", "bus-group");
        propsConsumerTx.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        propsConsumerTx.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class.getName());
        propsConsumerTx.put("isolation.level", "read_committed");
        propsConsumerTx.put("enable.auto.commit", "false");
        propsConsumerTx.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        String input_topic = "Roosevelt";
        consumerFromBus = new KafkaConsumer<String, Bus>(propsConsumerTx);
        consumerFromBus.subscribe(Collections.singletonList(input_topic),
                new ConsumerRebalanceListener() {
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                        //System.out.println("onPartitionsRevoked")
                    }
                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                        System.out.println("currentNumber before rebalance: " + currentNumber);
                        currentNumber = new HashMap<String, Long>();
                    }});

        //consumer consume from current
        Properties propsConsumerLocalBalance = new Properties();
        propsConsumerLocalBalance.put("bootstrap.servers", "localhost:9092");
        propsConsumerLocalBalance.put("group.id", "current-group");
        propsConsumerLocalBalance.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        propsConsumerLocalBalance.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaJsonDeserializer.class.getName());
        propsConsumerLocalBalance.put("isolation.level", "read_committed");
        propsConsumerLocalBalance.put("enable.auto.commit", "false");
        propsConsumerLocalBalance.put("fetch.max.bytes", 0);
        consumerFromCurrent =
                new KafkaConsumer<String, BusCurrent>(propsConsumerLocalBalance);
    }

    private static void InitProducer() {
        //producer produce to balance
        Properties propsTxWrite = new Properties();
        propsTxWrite.put("bootstrap.servers", "localhost:9092");
        propsTxWrite.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        propsTxWrite.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());
        propsTxWrite.put("transactional.id", UUID.randomUUID().toString()); //Should be different between validators to avoid being fenced due to same transactional.id.
        propsTxWrite.put("enable.idempotence", "true");
        producer = new KafkaProducer<>(propsTxWrite);
    }

    private static void PollFromCurrent(Bus tx) {
        if (!currentNumber.containsKey(tx.getCarNumber())) {
            TopicPartition topicPartition = new TopicPartition("crowdedness", Match(carNumberList,tx.getCarNumber()));
            consumerFromCurrent.assign(List.of(topicPartition));
            consumerFromCurrent.seekToEnd(Collections.singleton(topicPartition));
            long latestOffset = consumerFromCurrent.position(topicPartition);
            boolean findingLast = true;
            while (findingLast) {
                consumerFromCurrent.seek(topicPartition, latestOffset);
                latestOffset -= 1;
                ConsumerRecords<String, BusCurrent> currentRecords = consumerFromCurrent.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, BusCurrent> currentRecord : currentRecords) {
                    String c = new Gson().toJson(currentRecord.value(), Map.class);
                    BusCurrent cRecord = gson.fromJson(c, BusCurrent.class);
                    currentNumber.compute(tx.getCarNumber(), (key, value) -> cRecord.getCurrentNum());
                    findingLast = false;
                }
            }
        }
    }

    private static Bus toBus(String r){
        String[] splitList = r.split(":|,|}");
        String[] split1 = splitList[1].split("\"");
        String[] split2 = splitList[2].split("\"");
        Bus bus = new Bus();
        if(split2[1].equals("down")){
            bus = new Bus(split1[1],Long.parseLong(splitList[3]),Long.parseLong(splitList[5]));
        }else {
            bus = new Bus(split1[1],Long.parseLong(splitList[5]),Long.parseLong(splitList[3]));
        }
        return bus;
    }

    private static Integer Match(ArrayList l, String num) {
        int par = l.indexOf(num);;
        return par;
    }

    private static BusCurrent Record(Bus tx) {
        return new BusCurrent(tx.getCarNumber(), currentNumber.get(tx.getCarNumber()));
    }

    private static void Process(Bus tx) throws ExecutionException, InterruptedException {
        currentNumber.compute(tx.getCarNumber(), (key, value) -> value - tx.getDown() + tx.getUp());
        producer.send(new ProducerRecord<String, BusCurrent>("crowdedness", Match(carNumberList,tx.getCarNumber()), tx.getCarNumber(), Record(tx)));
    }
}
