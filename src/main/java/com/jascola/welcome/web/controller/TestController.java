package com.jascola.welcome.web.controller;

import com.github.pagehelper.PageInfo;
import com.jascola.welcome.web.dao.TestDao;
import com.jascola.welcome.web.entity.TestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class TestController {

    private final RedisTemplate<String,String> redisTemplate;
    private final TestDao testDao;
    private Logger logger = LogManager.getLogger("com.jascola.welcome.web.controller.TestController");
    public TestController(TestDao testDao, RedisTemplate<String, String> redisTemplate) {
        this.testDao = testDao;
        this.redisTemplate = redisTemplate;
    }

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("title","测试");
        TestEntity testEntity = new TestEntity();
        testEntity.setMyMoney(Money.of(CurrencyUnit.of("CNY"), 100000000L));
        testEntity.setUserName("jascola");
        testEntity.setPageParam(1,1);
        //testDao.insert(testEntity);
        modelAndView.addObject("entities",testDao.getTestEntity(testEntity));
        logger.info(new PageInfo<>(testDao.getTestEntity(testEntity)));
        logger.info(testDao.getTestEntityRow(new RowBounds(1,1)));
        return modelAndView;
    }

    @RequestMapping("/redis")
    @ResponseBody
    public String redis(){
        TestEntity testEntity = new TestEntity();
        testEntity.setMyMoney(Money.of(CurrencyUnit.of("CNY"), 100000000L));
        testEntity.setUserName("jascola");
        testEntity.setPageParam(1,1);
        //testDao.insert(testEntity);
        List<TestEntity> list = testDao.getTestEntity(testEntity);
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get("test-ket");
        if(StringUtils.isNotBlank(value)){
            redisTemplate.delete("test-ket");
            logger.info("test-ket");
            return value;
        }else{
            valueOperations.set("test-ket","jsonString");
            redisTemplate.expire("test-ket",1, TimeUnit.MINUTES);
            return "jsonString";
        }
    }
}
