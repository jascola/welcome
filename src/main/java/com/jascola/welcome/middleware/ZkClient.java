package com.jascola.welcome.middleware;

import io.netty.util.CharsetUtil;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Component
public class ZkClient {

    @Value("${zookeeper.server-addr}")
    private String zkService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ZkClient.class);
    private static Stat stat = new Stat();
    private static ZooKeeper zooKeeper =null;
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    @PostConstruct
    private void init(){
        try {
            zooKeeper =new ZooKeeper(zkService, 5000, watchedEvent -> {
                if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {  //zk连接成功通知事件
                    if (Watcher.Event.EventType.None == watchedEvent.getType() && null == watchedEvent.getPath()) {
                        connectedSemaphore.countDown();
                    } else if (watchedEvent.getType() == Watcher.Event.EventType.NodeDataChanged) {  //zk目录节点数据变化通知事件
                        try {
                            logger.info("{}",new String(zooKeeper.getData(watchedEvent.getPath(), true, stat)));
                        } catch (Exception e) {
                            logger.error("{}",e);
                        }
                    }
                }
            });
        } catch (IOException e) {
            logger.error("{}",e);
        }
    }

    public void createNode(String path){
        try {
            path = "/" + path;
            zooKeeper.create(path, "Hello world".getBytes(CharsetUtil.UTF_8),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            logger.error("{}",e);
        }
    }

    public String getNodeData(String path){
        byte[] data = new byte[0];
        try {
            path = "/" + path;
            logger.info("getVersion==========={}",stat.getVersion());
            data = zooKeeper.getData(path, true, stat);
        } catch (KeeperException | InterruptedException e) {
            logger.error("{}",e);
        }
        return new String(data);
    }

    /**
     * 更新前需要先获取当前stat信息
     * */
    public String updateNodeData(String value,String path){
        try {
            path = "/" + path;
            logger.info("updateVersion==========={}",stat.getVersion());
           Stat statV = zooKeeper.setData(path,value.getBytes(CharsetUtil.UTF_8),stat.getVersion());
            return statV.getVersion()+"";
        } catch (KeeperException | InterruptedException e) {
            logger.error("{}",e);
        }
        return "failed";
    }

    public void deleteNodeData(String path){
        try {
            path = "/" + path;
            logger.info("deleteVersion==========={}",stat.getVersion());
            zooKeeper.delete(path,stat.getVersion());
        } catch (KeeperException | InterruptedException e) {
            logger.error("{}",e);
        }
    }
}
