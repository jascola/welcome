package com.jascola.welcome.web.controller;

import com.jascola.welcome.web.dao.TestDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
        modelAndView.addObject("entities",testDao.getTestEntity());
        logger.info(testDao.getTestEntity());
        return modelAndView;
    }
}
