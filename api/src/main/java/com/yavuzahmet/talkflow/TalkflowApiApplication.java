package com.yavuzahmet.talkflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TalkflowApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkflowApiApplication.class, args);
	}

}
