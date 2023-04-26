package com.traders.traders;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/kafka/test")
public class SampleController {
	private final KafkaProducer producer;

	@PostMapping(value = "/message")
	public String sendMessage(@RequestParam("message") String message) {
		this.producer.sendMessage(message);
		return "success";
	}
}