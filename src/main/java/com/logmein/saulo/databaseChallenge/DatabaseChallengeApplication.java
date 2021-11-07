package com.logmein.saulo.databaseChallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(
		basePackages = {"com.logmein.saulo.databaseChallenge.server.entity"}
)
@EnableJpaRepositories(
		basePackages = {"com.logmein.saulo.databaseChallenge.server.repository"}
)
public class DatabaseChallengeApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseChallengeApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
	{
		return application.sources(DatabaseChallengeApplication.class);
	}

}
