package com.traders.traders;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class KafkaConsumer {

	@KafkaListener(topics = "quickstart-events", groupId = ConsumerConfig.GROUP_ID_CONFIG)
	public void consume(String message) throws IOException {
		log.info(String.format("Consumed message : %s", message));
	}
}