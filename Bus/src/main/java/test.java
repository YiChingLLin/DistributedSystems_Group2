import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.apache.kafka.clients.admin.*;
public class test {
    public static void main(String[] args) throws Exception {
        //props
        System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "off"); //"off", "trace", "debug", "info", "warn", "error".

        // initialize
        Properties producerProps = new Properties();
        producerProps.put("bootstrap.servers", "localhost:9092");
        producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaJsonSerializer.class.getName());
        Producer<String, Bus> producer = new KafkaProducer<>(producerProps);

        Bus b1 = new Bus("NCCU-001", 8, 5);
        producer.send(new ProducerRecord<String, Bus>("Roosevelt", 0, b1.getCarNumber(), b1)).get();
        Bus b2 = new Bus("NCCU-002", 5L, 10L);
        producer.send(new ProducerRecord<String, Bus>("Roosevelt", 1, b2.getCarNumber(), b2)).get();
        Bus b3 = new Bus("NCCU-003", 3L,13L);
        producer.send(new ProducerRecord<String, Bus>("Roosevelt", 2, b3.getCarNumber(), b3)).get();

//        Bus b1 = new Bus("NCCU-001", 5, 10);
//        producer.send(new ProducerRecord<String, Bus>("Roosevelt", 0, b1.getCarNumber(), b1)).get();
//        Bus b2 = new Bus("NCCU-002", 15L, 7L);
//        producer.send(new ProducerRecord<String, Bus>("Roosevelt", 1, b2.getCarNumber(), b2)).get();
//        Bus b3 = new Bus("NCCU-003", 13L,1L);
//        producer.send(new ProducerRecord<String, Bus>("Roosevelt", 2, b3.getCarNumber(), b3)).get();


        System.out.println("Send.");
    }
}
