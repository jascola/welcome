package com.jascola.welcome.aop;

import com.jascola.welcome.aop.bar.BarConfig;
import com.jascola.welcome.aop.foo.FooConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTestMain {

    public static void main(String[] args) {
        ApplicationContext apx = new AnnotationConfigApplicationContext(FooConfig.class);
        ApplicationContext apy = new AnnotationConfigApplicationContext(BarConfig.class);
        ((AnnotationConfigApplicationContext) apy).setParent(apx);

        TestBean bean = apx.getBean("testBeanX",TestBean.class);
        bean.say();

        bean = apy.getBean("testBeanX",TestBean.class);
        bean.say();

        bean = apy.getBean("testBeanY",TestBean.class);
        bean.say();
    }
}
