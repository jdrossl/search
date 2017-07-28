package org.craftercms.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:search-services.xml")
public class SearchApplicationMain {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplicationMain.class);
    }

}
