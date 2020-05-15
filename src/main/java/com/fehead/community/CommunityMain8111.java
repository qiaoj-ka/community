package com.fehead.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableTransactionManagement //开启声明式事务
@MapperScan("com.fehead.community.mapper")
public class CommunityMain8111
{
    public static void main( String[] args )
    {
        SpringApplication.run(CommunityMain8111.class,args);
    }
}
