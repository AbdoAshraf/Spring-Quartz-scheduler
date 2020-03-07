package com.quartz.demo.config;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.quartz.demo.stream.GreetingsStreams;

@EnableBinding(GreetingsStreams.class)
public class StreamsConfig {
}
