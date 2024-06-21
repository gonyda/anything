package com.bbsk.anything.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "anything")
public class GlobalProperties {
    private final Chrome chrome;

    @Getter
    @RequiredArgsConstructor
    public static class Chrome {
        private final Driver driver;

        @Getter
        @RequiredArgsConstructor
        public static class Driver {
            private final String path;
        }
    }
}
