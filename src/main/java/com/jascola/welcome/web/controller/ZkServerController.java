package com.jascola.welcome.web.controller;

import com.jascola.welcome.middleware.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZkServerController {

    private final ZkClient zkClient;

    @Autowired
    public ZkServerController(ZkClient zkClient) {
        this.zkClient = zkClient;
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
