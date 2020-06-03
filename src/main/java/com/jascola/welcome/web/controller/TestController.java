package com.jascola.welcome.web.controller;

import com.github.pagehelper.PageInfo;
import com.jascola.welcome.web.dao.TestDao;
import com.jascola.welcome.web.entity.TestEntity;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    private final TestDao testDao;
    private Logger logger = LogManager.getLogger("com.jascola.welcome.web.controller.TestController");

    public TestController(TestDao testDao) {
        this.testDao = testDao;
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
}
