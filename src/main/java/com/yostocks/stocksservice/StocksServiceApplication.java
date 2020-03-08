package com.yostocks.stocksservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
@EnableScheduling
@EnableAsync
public class StocksServiceApplication {

    @Autowired
    private CacheManager cacheManager;


    public static void main(String[] args) {
        SpringApplication.run(StocksServiceApplication.class, args);
    }

    /**
     * https://docs.oracle.com/javase/6/docs/api/java/util/concurrent/ThreadPoolExecutor.html
     */

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(75);
        executor.setMaxPoolSize(75);
        executor.setQueueCapacity(3000);
        executor.setThreadNamePrefix("AllStocksLookup-");
        executor.setAllowCoreThreadTimeOut(true);
        executor.initialize();
        return executor;
    }

    // scheduling cache clearance
    // this configuration affects only 'CurrencyExchangeService' and 'YahooFinanceService
    @Scheduled(fixedRate = 3600000)              // execute after every 30 min
    public void clearCacheSchedule() {
        for (String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();            // clear cache by name
        }
        System.out.println("***********  " + "cache has been cleared" + "  ************");
    }

    @Bean
    @LoadBalanced // Thread-safe: https://spring.io/blog/2009/03/27/rest-in-spring-3-resttemplate
    RestTemplate restTemplate() {
        return new RestTemplate();
    }


}