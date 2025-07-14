package io.github.Mine4Cut.Mine4Cut_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Mine4CutServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Mine4CutServerApplication.class, args);
	}

}
