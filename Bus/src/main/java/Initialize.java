import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.apache.kafka.clients.admin.*;

public class Initialize {
    public static void main(String[] args) throws Exception {
        //props
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off"); //"off", "trace", "debug", "info", "warn", "error".

        // initialize
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());
        Producer<String, BusCurrent> producer = new KafkaProducer<>(producerProps);

        BusCurrent cur1 = new BusCurrent("NCCU-001", 50L);
        producer.send(new ProducerRecord<String, BusCurrent>("crowdedness", 0, cur1.getCarNo(), cur1)).get();
        BusCurrent cur2 = new BusCurrent("NCCU-002", 50L);
        producer.send(new ProducerRecord<String, BusCurrent>("crowdedness", 1, cur2.getCarNo(), cur2)).get();
        BusCurrent cur3 = new BusCurrent("NCCU-003", 50L);
        producer.send(new ProducerRecord<String, BusCurrent>("crowdedness", 2, cur3.getCarNo(), cur3)).get();


        System.out.println("Bus current people number has been initialized.");
    }
}
