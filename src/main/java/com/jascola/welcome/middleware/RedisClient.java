package com.jascola.welcome.middleware;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * redis
 */

@Component
public class RedisClient {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RedisClient.class);

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisClient(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String get(String key){
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] bytes = connection.get(encode(key));
            return decode(bytes);
        });
    }

    public Boolean set(String key,String value){
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] bytes = serializer.serialize(value);
            return connection.set(encode(key),bytes);
        });
    }

    public Boolean setNX(String key,String value){
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] bytes = serializer.serialize(value);
            return connection.setNX(encode(key),bytes);
        });
    }

    public String hGet(String key,String filed){
        return redisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] bytes = connection.hGet(encode(key),encode(filed));
            return decode(bytes);
        });
    }

    public Boolean hSet(String key,String filed,String value){
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
            byte[] bytes = serializer.serialize(value);
            return connection.hSet(encode(key),encode(filed),bytes);
        });
    }

    public Long delete(String key){
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.del(encode(key)));
    }

    public Boolean expire(String key,long seconds){
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.expire(encode(key),seconds));
    }

    private String decode(byte[] bytes){
        if(null==bytes){
            return null;
        }
        try {
            return new String(bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private byte[] encode(String value){
        if(null==value) {
            throw new RuntimeException(
                    "value sent to redis cannot be null");
        }
        try {
            return value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(
                    e);
        }
    }
}
