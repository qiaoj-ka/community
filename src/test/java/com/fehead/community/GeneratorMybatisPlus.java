//package com.fehead.community;
//
//import com.baomidou.mybatisplus.annotation.DbType;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
//import com.baomidou.mybatisplus.generator.config.GlobalConfig;
//import com.baomidou.mybatisplus.generator.config.PackageConfig;
//import com.baomidou.mybatisplus.generator.config.StrategyConfig;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//
//public class GeneratorMybatisPlus {
//    public static void main(String[] args) {
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "/src/main/java");
//        gc.setAuthor("ktoking");
//        gc.setOpen(false);
//        gc.setFileOverride(true);//是否覆盖
//        // gc.setSwagger2(true); 实体属性 Swagger2 注解
//        mpg.setGlobalConfig(gc);
//
//        //数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://39.96.86.4:3308/community?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC");
//        // dsc.setSchemaName("public");
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        dsc.setUsername("root");
//        dsc.setPassword("kaikai");
//        dsc.setDbType(DbType.MYSQL);
//        mpg.setDataSource(dsc);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("com.fehead.community");
//        pc.setController("controller");
//        pc.setEntity("entities");
//        pc.setMapper("mapper");
//        pc.setService("service");
//        mpg.setPackageInfo(pc);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        // 数据库表映射到实体的命名策略: 下划线转驼峰命名
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        // 数据库表字段映射到实体的命名策略: 下划线转驼峰命名
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        // 【实体】是否为lombok模型（默认 false）
//        strategy.setEntityLombokModel(true);
//        // 需要包含的表名，允许正则表达式（与exclude二选一配置）
//        strategy.setInclude("activity");
//        mpg.setStrategy(strategy);
//
//        mpg.execute();
//
//
//    }
//}
