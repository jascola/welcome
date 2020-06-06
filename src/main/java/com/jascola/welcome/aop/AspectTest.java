package com.jascola.welcome.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class AspectTest {
    private Logger logger = LogManager.getLogger("com.jascola.welcome.aop.AspectTest");
    @AfterReturning("bean(testBean*)")
    public void sayAfter(){
        logger.info("aspect has worked");
    }
}
