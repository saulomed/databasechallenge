package com.logmein.saulo.databaseChallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(
		basePackages = {"com.logmein.saulo.databaseChallenge.server.entity"}
)
@EnableJpaRepositories(
		basePackages = {"com.logmein.saulo.databaseChallenge.server.repository"}
)
public class DatabaseChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseChallengeApplication.class, args);
	}

}
