package com.fehead.community;

import static org.junit.Assert.assertTrue;


import com.fehead.community.utility.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * Unit test for simple CommunityMain8111.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void addInfo(){
        System.out.println(111);
    }

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void setRedisKey(){
        redisUtil.set("test",1);
        for (int i=0;i<10;i++){
            if(redisUtil.hasKey("test")){
                redisUtil.incr("test",1);
            }
            System.out.println(redisUtil.get("test"));
        }
        Object result=redisUtil.get("test");
        System.out.println(result.toString());
    }

}
