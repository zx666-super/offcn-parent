package com.offcn;

import com.offcn.user.UserStartApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {UserStartApplication.class})
public class ScwUserApplicationTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void TestRedis(){
        redisTemplate.boundValueOps("name").set("zxxx");
        Object name = redisTemplate.boundValueOps("name").get();
        System.out.println("name"+name);
        stringRedisTemplate.opsForValue().set("name2","lisiiiii");
        String name2 = stringRedisTemplate.opsForValue().get("name2");
        System.out.println("stringRedisTemplate存储"+name2);
    }
}
