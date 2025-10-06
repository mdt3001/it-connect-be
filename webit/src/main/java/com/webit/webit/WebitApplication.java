package com.webit.webit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class WebitApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebitApplication.class, args);
	}

}
