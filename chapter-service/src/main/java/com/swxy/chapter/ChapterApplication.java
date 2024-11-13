package com.swxy.chapter;
import com.swxy.api.config.DefaultFeignConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.core.env.ConfigurableEnvironment;

@EnableFeignClients(basePackages = "com.swxy.api.client",defaultConfiguration = DefaultFeignConfig.class )
@Slf4j
@SpringBootApplication
@MapperScan("com.swxy.chapter.mapper")
public class ChapterApplication {
    public static void main(String[] args) {
        ConfigurableEnvironment env=SpringApplication.run(ChapterApplication.class, args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application: '{}' is running Success! \n\t" +
                        "Local URL: \thttp://localhost:{}\n\t" +
                        "Document:\thttp://localhost:{}/doc.html\n" +
                        "----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                env.getProperty("server.port"));
    }
}