package com.bbsk.anything;

import com.bbsk.anything.javis.constant.ChatGptConfig;
import com.bbsk.anything.naver.news.constant.NaverConfig;
import com.bbsk.anything.properties.GlobalProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling // 스케줄링
@EnableConfigurationProperties(GlobalProperties.class)
@SpringBootApplication
@EnableCaching // 캐시 활성화
public class AnythingApplication implements CommandLineRunner {

    private final NaverConfig naverConfig;
    private final ChatGptConfig chatGptConfig;

    public AnythingApplication(NaverConfig naverConfig, ChatGptConfig chatGptConfig) {
        this.naverConfig = naverConfig;
        this.chatGptConfig = chatGptConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(AnythingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        naverConfig.injectIntoEnum();
        chatGptConfig.injectIntoEnum();
    }

}
