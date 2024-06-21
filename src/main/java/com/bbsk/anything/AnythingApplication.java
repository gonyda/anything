package com.bbsk.anything;

import com.bbsk.anything.properties.GlobalProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties(GlobalProperties.class)
@SpringBootApplication
public class AnythingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnythingApplication.class, args);
	}

}
