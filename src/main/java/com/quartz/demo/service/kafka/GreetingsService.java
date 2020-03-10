package com.quartz.demo.service.kafka;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.quartz.demo.dto.Greetings;
import com.quartz.demo.stream.GreetingsStreams;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GreetingsService {
	private final GreetingsStreams greetingsStreams;

	public GreetingsService(GreetingsStreams greetingsStreams) {
		this.greetingsStreams = greetingsStreams;
	}

	public void sendGreeting(final Greetings greetings) {
		log.info("Sending greetings {}", greetings);
		greetingsStreams.outboundGreetings().send(MessageBuilder.withPayload(greetings)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}
}