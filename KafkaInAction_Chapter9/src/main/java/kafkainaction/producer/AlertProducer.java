package kafkainaction.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import kafkainaction.callback.AlertCallback;
import kafkainaction.model.Alert;

public class AlertProducer {
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {

		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092,localhost:9093");
		props.put("key.serializer", "com.kafkainaction.serde.AlertKeySerde");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		//props.put("partitioner.class", 				"com.kafkainaction.partitioner.AlertLevelPartitioner"); 
		props.put(
				   "interceptor.classes",
				   "kafkainaction.producer.AlertProducerMetricsInterceptor");

		Producer<Alert, String> producer = new KafkaProducer<Alert, String>(props);
		Alert alert = new Alert(1, "Stage 1", "CRITICAL", "Stage 1 stopped");
		ProducerRecord<Alert, String> producerRecord = new ProducerRecord<Alert, String>("alert", alert, alert.getAlertMessage()); // #A <1>
		 
		producer.send(producerRecord, new AlertCallback());

		producer.close();
	}

}
