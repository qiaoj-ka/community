package com.fehead.community;

import com.fehead.community.entities.Activity;
import com.fehead.community.mapper.ActivityMapper;
import com.fehead.community.utility.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

import static org.junit.Assert.assertTrue;


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

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void setRedisKey(){
        redisUtil.set("test",15);
        //stringRedisTemplate.opsForValue().set("test","15");
        System.out.println(redisUtil.get("test"));
        for (int i=0;i<10;i++){
            if(redisUtil.hasKey("test")){
                redisUtil.decr("test",1);
            }
            System.out.print(redisUtil.get("test")+"->");
        }
        System.out.println();
        stringRedisTemplate.opsForValue().set("jiajia","17");
        System.out.println(stringRedisTemplate.opsForValue().get("jiajia"));
        for (int i = 0; i <3 ; i++) {
            Long num=stringRedisTemplate.opsForValue().decrement("jiajia");
            System.out.print("num:"+num+"->");
            System.out.println("value:"+stringRedisTemplate.opsForValue().get("jiajia"));
        }

    }
    @Test
    public void getToday(){
        Date date=new Date();
        long time1 = System.currentTimeMillis();
        System.out.println(time1);
        LocalDateTime localDateTime=LocalDateTime.now();
        long time111=localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(time111);
        //java获取秒级时间戳
        //方法：获取毫秒级世纪初哦 除以1000（四舍五入）后，获取秒级时间戳

    }

    @Resource
    private ActivityMapper activityMapper;

    @Test
    public void tttt(){
        Activity activitie=activityMapper.selectById(1);
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm:ss");
        System.out.println(activitie.getActivityEndtime().toString());
        //System.out.println(activitie.getActivityEndtime().format(formatter));
    }
    @Test
    public void testTime(){
        Activity activitie=activityMapper.selectById(1);
        LocalDateTime dateTime=activitie.getActivityStarttime();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyyMMdd");
        String now=LocalDateTime.now(ZoneOffset.of("+8")).format(formatter);
        String date=dateTime.format(formatter);
        System.out.println(date+"\t"+now);
        Integer month1=Integer.parseInt(date.substring(4,6));
        Integer month2=Integer.parseInt(now.substring(4,6));
        StringBuilder result=new StringBuilder();
        if(month2-month1>0){
            result.append((month2-month1)+"个月前");
        }else {

        }
        Integer day1=Integer.parseInt(date.substring(6,8));
        Integer day2=Integer.parseInt(now.substring(6,8));
        if(day2-day1>=0){
            result.append((day2-day1)+"天前");
        }else{
            result.append("今天");
        }
        System.out.println(result.toString());
    }

    @Test
    public void timeTest(){
        Activity activity=activityMapper.selectById(1);
        Long timestap=LocalDateTime.now().toInstant(ZoneOffset.of("+8")).getEpochSecond();
        Long time=activity.getActivityStarttime().toInstant(ZoneOffset.of("+8")).getEpochSecond();
        Long cha=(timestap-time)/(60*60*24);
        if(cha/30>0){
            System.out.println(cha/30+"月前");
        }else {
            System.out.println((timestap-time)/(60*60*24)+"天前");
        }

        //按照天
        LocalDateTime now=LocalDateTime.now();
        LocalDateTime start=activity.getActivityStarttime();
        Duration duration=Duration.between(start,now);
        System.out.println(duration.toDays());

        //获取系统默认时区（可以按照传入偏移量计算获取 时区）
        ZoneId zoneId=ZoneId.systemDefault();
        //当前服务器时间
        LocalDateTime now1=LocalDateTime.now(zoneId);
        //计算时间插值按照天
        LocalDateTime time1=now1.minusDays(duration.toDays()).with(LocalDateTime.MIN);
        System.out.println(time1);
        //计算时间插值按照周
        //计算时间差之按照月
    }

    @Test
    public void redisTest(){
        String hotkey=1+"hot";
        if(!redisUtil.hasKey(hotkey)){
            redisUtil.set(hotkey,1);
        }else {
            redisUtil.incr(hotkey,1);
        }
        System.out.println(redisUtil.get(hotkey));
    }

    @Test
    public void hello1(){
        Activity activity=activityMapper.selectById(24);
        //活动开始的当天0点
        LocalDateTime minTime=LocalDateTime.of(activity.getActivityStarttime().toLocalDate(), LocalTime.MIN);
        //现在的时间
        LocalDateTime now=LocalDateTime.now();
        //现在时间的0.00
        LocalDateTime nowMin=LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
        //表示活动0.00与开始时间做比较
        Duration duration2=Duration.between(minTime,now);
        //活动开始0.00和当现在0点
        Duration duration4=Duration.between(nowMin,minTime);
        //活动今天开始
        if(duration2.toMinutes()>0&&duration4.toDays()==0){
            System.out.println("活动今天开始");
        }else if(duration4.toDays()<0){//表示时间还没到
            System.out.println(duration4.toDays()*-1+"天前");
        }else if(duration4.toDays()>0&&duration4.toDays()<30){
            System.out.println("距离活动开始还有："+duration4.toDays()+"天");
        }else if(duration4.toDays()>30){
            System.out.println(duration4.toDays()/30+"个月前");
        }
    }

    @Test
    public void getLong(){
        Long timestamp=LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String dateTimeStr = "2018-07-28 14:11:15";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, df);
        Long endtime=dateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
        System.out.println(timestamp);
        System.out.println(endtime);

    }

}
