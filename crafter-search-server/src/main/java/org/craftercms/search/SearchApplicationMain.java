package org.craftercms.search;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@SpringBootApplication
@ImportResource("classpath:search-services.xml")
public class SearchApplicationMain extends WebMvcConfigurerAdapter {

    @Value("${search.server.port}")
    private int serverPort;
    @Value("${search.server.contextPath}")
    private String serverContextPath;
    @Value("${search.server.log.accesslog.enable}")
    private boolean serverAccessLogEnable;
    @Value("${search.server.log.dir}")
    private String serverAccessLogDir;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SearchApplicationMain.class);
        app.setWebEnvironment(true);
        app.run(args);
    }


    @Bean
    public UndertowEmbeddedServletContainerFactory getEmbeddedServletContainerFactory() throws IOException {
        UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory =
                new UndertowEmbeddedServletContainerFactory(serverPort);
        embeddedServletContainerFactory.setContextPath(serverContextPath);
        embeddedServletContainerFactory.setAccessLogEnabled(serverAccessLogEnable);
        if(serverAccessLogEnable){
            Path sald = Paths.get(serverAccessLogDir);
            if(!Files.isDirectory(sald)){
                Files.createDirectories(sald);
            }
            embeddedServletContainerFactory.setAccessLogDirectory(sald.toFile());
        }
        return embeddedServletContainerFactory;
    }

}
