package com.raven.appserver.config;

import com.alibaba.druid.pool.DruidDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DruidConfig {
    @Value("${druid.url}")
    private String url;
    @Value("${druid.username}")
    private String username;
    @Value("${druid.password}")
    private String password;
    @Value("${druid.driverClassName}")
    private String driverClassName;
    @Value("${druid.maxActive}")
    private int maxActive;
    @Value("${druid.minIdle}")
    private int minIdle;
    @Value("${druid.initialSize}")
    private int initialSize;
    @Value("${druid.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${druid.maxWait}")
    private int maxWait;
    @Value("${druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${druid.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${druid.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${druid.testOnReturn}")
    private boolean testOnReturn;
    @Value("${druid.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${druid.validationQuery}")
    private String validationQuery;
    @Value("${druid.filters}")
    private String filters;
    @Value("${druid.connectionProperties}")
    private String connectionProperties;

    @Bean
    @Primary  //在同样的DataSource中，首先使用被标注的DataSource
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        //configuration
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery(validationQuery);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        dataSource.setValidationQuery(validationQuery);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        dataSource.setConnectionProperties(connectionProperties);
        return dataSource;
    }
}
