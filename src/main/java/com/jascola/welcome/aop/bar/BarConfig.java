package com.jascola.welcome.aop.bar;

import com.jascola.welcome.aop.AspectTest;
import com.jascola.welcome.aop.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class BarConfig {

    @Bean("testX")
    public TestBean testX(){
        return new TestBean("bar");
    }

    /*@Bean
    public AspectTest aspectTest(){
        return new AspectTest();
    }*/
}
