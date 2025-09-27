package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import vn.iotstar.config.StorageProperties;
import vn.iotstar.service.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class Baitap8Application {
    public static void main(String[] args) {
        SpringApplication.run(Baitap8Application.class, args);
    }
    @Bean
    CommandLineRunner init(IStorageService storageService) {
        return args -> storageService.init();
    }
}
