package com.vlad.tech.inventoryservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import javax.sql.DataSource;

//@Configuration
public class DatasourceConfig {

//    private Environment env;
//
//    @Autowired
//    private DatasourceConfig(Environment env){
//        this.env = env;
//    }

//    @Bean("userDs")
//    @Primary
//    public DataSource dataSource(){
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
//        ds.setUrl(env.getProperty("user.datasource.url"));
//        ds.setUsername(env.getProperty("user.datasource.username"));
//        ds.setPassword(env.getProperty("user.datasource.password"));
//        return ds;
//    }
}
