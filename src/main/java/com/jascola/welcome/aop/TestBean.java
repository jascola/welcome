package com.jascola.welcome.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestBean {

    private Logger logger = LogManager.getLogger("com.jascola.welcome.aop.TestBean");
    private String context;

    public TestBean(String context) {
        this.context = context;
    }
    public void say(){
        logger.info("hello----{}",context);
    }

}
