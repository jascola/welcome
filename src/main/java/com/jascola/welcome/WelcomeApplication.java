package com.jascola.welcome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@SpringBootApplication
public class WelcomeApplication extends SpringBootServletInitializer implements ApplicationRunner {

    @Autowired
    private RestTemplate restTemplate;


    public static void main(String[] args) {
        SpringApplication.run(WelcomeApplication.class, args);
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());
        return template;
    }

    @Bean
    public ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        return new ReactiveStringRedisTemplate(reactiveRedisConnectionFactory);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder().build();
    }
    /**
     * 打成war包 需要继承 SpringBootServletInitializer
     * */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder  application){
        return application.sources(WelcomeApplication.class);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        URI uri = UriComponentsBuilder.
                fromUriString("http://10.24.19.100:8080/welcome-0.0.1-SNAPSHOT/index").
                build().toUri();
        String str = restTemplate.getForObject(uri,String.class);
        System.out.println(str);
    }
}
