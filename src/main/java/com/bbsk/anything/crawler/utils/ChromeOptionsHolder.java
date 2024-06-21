package com.bbsk.anything.crawler.utils;

import org.openqa.selenium.chrome.ChromeOptions;

public enum ChromeOptionsHolder {

    INSTANCE;

    private final ChromeOptions chromeOptions;

    ChromeOptionsHolder() {
        this.chromeOptions = create();
    }

    private static ChromeOptions create() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--lang=ko");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-dev-shm-usage");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--headless");
        return chromeOptions;
    }

    public ChromeOptions get() {
        return this.chromeOptions;
    }
}
