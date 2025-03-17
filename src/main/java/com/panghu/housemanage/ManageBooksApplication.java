package com.panghu.housemanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
@MapperScan("com.panghu.housemanage.dao")
public class ManageBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageBooksApplication.class, args);
    }
    
    /**
     * 配置RestTemplate Bean用于HTTP请求
     * 设置连接和读取超时
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(60000))
                .setReadTimeout(Duration.ofMillis(60000))
                .build();
    }
    
    /**
     * 配置ClientHttpRequestFactory
     * 用于更高级的HTTP客户端配置
     * @return ClientHttpRequestFactory实例
     */
    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60000);
        factory.setReadTimeout(60000);
        factory.setBufferRequestBody(false);
        return factory;
    }
}
