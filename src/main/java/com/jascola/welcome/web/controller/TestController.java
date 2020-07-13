package com.jascola.welcome.web.controller;

import com.github.pagehelper.PageInfo;
import com.jascola.welcome.aop.TestBean;
import com.jascola.welcome.middleware.ZkClient;
import com.jascola.welcome.web.dao.TestDao;
import com.jascola.welcome.web.entity.TestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Controller
public class TestController {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;
    private final RedisTemplate<String, String> redisTemplate;
    private final TestDao testDao;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TestController.class);
    private final TestBean bean;
    private final ZkClient zkClient;

    @Autowired
    public TestController(TestDao testDao, RedisTemplate<String, String> redisTemplate, ReactiveStringRedisTemplate reactiveStringRedisTemplate, @Qualifier("testBeanY") TestBean bean, ZkClient zkClient) {
        this.testDao = testDao;
        this.redisTemplate = redisTemplate;
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.bean = bean;
        this.zkClient = zkClient;
    }

    @RequestMapping("/index")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("title", "测试");
        TestEntity testEntity = new TestEntity();
        testEntity.setMyMoney(Money.of(CurrencyUnit.of("CNY"), 100000000L));
        testEntity.setUserName("jascola");
        testEntity.setPageParam(1, 1);
        //testDao.insert(testEntity);
        modelAndView.addObject("entities", testDao.getTestEntity(testEntity));
        logger.info("{}",new PageInfo<>(testDao.getTestEntity(testEntity)));
        logger.info("{}",testDao.getTestEntityRow(new RowBounds(1, 1)));
        return modelAndView;
    }

    @RequestMapping("/redis")
    @ResponseBody
    public String redis() {
        TestEntity testEntity = new TestEntity();
        testEntity.setMyMoney(Money.of(CurrencyUnit.of("CNY"), 100000000L));
        testEntity.setUserName("jascola");
        testEntity.setPageParam(1, 1);
        //testDao.insert(testEntity);
        List<TestEntity> list = testDao.getTestEntity(testEntity);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get("test-ket");
        if (StringUtils.isNotEmpty(value)) {
            redisTemplate.delete("test-ket");
            logger.info("test-ket");
            return value;
        } else {
            valueOperations.set("test-ket", "jsonString");
            redisTemplate.expire("test-ket", 1, TimeUnit.MINUTES);
            return "jsonString";
        }
    }

    @RequestMapping("/reactor")
    @ResponseBody
    public String reactor() {
        Flux.just("jascola", "linda", "mother", "xi")
                .publishOn(Schedulers.elastic())
                .doOnRequest(n -> logger.info("on request----{}", n))
                .filter(n -> n.length() > 3 && n.length() < 10)
                .map(n -> {
                    if (n.length() < 10) {
                        n = n + "good luck";
                    }
                    logger.info("map thread----{}", Thread.currentThread());
                    return n;
                })
                .subscribe(n -> logger.info("reactor----{}----{}", n,Thread.currentThread().getName()),
                        e -> logger.info("exception----{}", e.toString()),
                        () -> logger.info("subscriber complete"),
                        s -> s.request(2));
        return "success";
    }

    @RequestMapping("/reactive/redis")
    @ResponseBody
    public String reactiveRedis() throws Exception {
        ReactiveHashOperations<String, String, String> hashOperations = reactiveStringRedisTemplate.opsForHash();
        List<TestEntity> list = testDao.getTestEntityAll();
        CountDownLatch cld = new CountDownLatch(1);
        Flux.fromIterable(list)
                .publishOn(Schedulers.single())
                .doOnComplete(() -> logger.info("list complete"))
                .flatMap(n -> hashOperations.put("redis-key", n.getId() + "", n.getUserName()))
                .concatWith(reactiveStringRedisTemplate.expire("redis-key", Duration.ofMinutes(1)))
                .subscribe(b -> logger.info("flux value----{}----{}", b,Thread.currentThread().getName()), e -> logger.info("error--{}", e), cld::countDown);

        logger.info("waiting----");
        cld.await();
        return "success";
    }

    @RequestMapping("/aspect")
    @ResponseBody
    public String aspect(){
        bean.say();
        return "success";
    }

    @RequestMapping("/zk/create")
    @ResponseBody
    public String createZkNode(String path){
        zkClient.createNode(path);
        return "success";
    }

    @RequestMapping("/zk/get")
    @ResponseBody
    public String getZkNode(String path) {
        return  zkClient.getNodeData(path);
    }

    @RequestMapping("/zk/update")
    @ResponseBody
    public String updateZkNode(String value,String path) {
        return  zkClient.updateNodeData(value,path);
    }

    @RequestMapping("/zk/delete")
    @ResponseBody
    public String deleteZkNode(String path) {
        zkClient.deleteNodeData(path);
        return "success";
    }
}
