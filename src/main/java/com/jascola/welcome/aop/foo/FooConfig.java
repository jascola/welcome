package com.jascola.welcome.aop.foo;

import com.jascola.welcome.aop.AspectTest;
import com.jascola.welcome.aop.TestBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class FooConfig {

    @Bean("testX")
    public TestBean testX(){
        return new TestBean("foo");
    }

    @Bean("testY")
    public TestBean testY(){
        return new TestBean("foo");
    }

    @Bean
    public AspectTest aspectTest(){
        return new AspectTest();
    }
}
