package com.bbsk.anything;

import com.bbsk.anything.javis.constant.ChatGptApi;
import com.bbsk.anything.javis.constant.ChatGptConfig;
import com.bbsk.anything.news.constant.NaverConfig;
import com.bbsk.anything.properties.GlobalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableConfigurationProperties(GlobalProperties.class)
@SpringBootApplication
public class AnythingApplication implements CommandLineRunner {

	@Autowired
	private NaverConfig naverConfig;
	@Autowired
	private ChatGptConfig chatGptConfig;

	public static void main(String[] args) {
		SpringApplication.run(AnythingApplication.class, args);
	}

	@Override
	public void run(String... args) {
		naverConfig.injectIntoEnum();
		chatGptConfig.injectIntoEnum();

		System.out.println(ChatGptApi.AUTHORIZATION.getValue());
	}

}
