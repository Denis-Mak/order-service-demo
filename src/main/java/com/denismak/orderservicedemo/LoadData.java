package com.denismak.orderservicedemo;

import com.denismak.orderservicedemo.bean.Order;
import com.denismak.orderservicedemo.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadData {
    private static Logger log = LoggerFactory.getLogger(LoadData.class);

    @Bean
    CommandLineRunner initDatabase(OrderRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Order("Pen", "Kooli OÜ")));
            log.info("Preloading " + repository.save(new Order("Ruler", "Kooli OÜ")));
        };
    }
}
