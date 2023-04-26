package com.traders.traders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

	@Value(value = "quickstart-events")
	private String topicName;

	private final KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String message) {
		log.info(String.format("Produced message : %s", message));
		this.kafkaTemplate.send(topicName, message);
	}
}