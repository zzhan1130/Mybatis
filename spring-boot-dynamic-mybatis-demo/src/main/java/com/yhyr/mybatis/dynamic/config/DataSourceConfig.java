package com.yhyr.mybatis.dynamic.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.yhyr.mybatis.dynamic.datasource.DynamicDataSource;
import com.yhyr.mybatis.dynamic.enums.DbType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YHYR on 2017-12-25
 */

@Configuration
public class DataSourceConfig {
    @Value("${spring.datasource.ds0.url}")
    private String ds0DBUrl;
    @Value("${spring.datasource.ds0.username}")
    private String ds0DBUser;
    @Value("${spring.datasource.ds0.password}")
    private String ds0DBPassword;
    @Value("${spring.datasource.ds0.driver-class-name}")
    private String ds0DBDreiverName;

    @Value("${spring.datasource.ds1.url}")
    private String ds1DBUrl;
    @Value("${spring.datasource.ds1.username}")
    private String ds1DBUser;
    @Value("${spring.datasource.ds1.password}")
    private String ds1DBPassword;
    @Value("${spring.datasource.ds1.driver-class-name}")
    private String ds1DBDreiverName;

    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();

        DruidDataSource defaultDataSource = new DruidDataSource();
        defaultDataSource.setUrl(ds0DBUrl);
        defaultDataSource.setUsername(ds0DBUser);
        defaultDataSource.setPassword(ds0DBPassword);
        defaultDataSource.setDriverClassName(ds0DBDreiverName);

        DruidDataSource masterDataSource = new DruidDataSource();
        masterDataSource.setDriverClassName(ds1DBDreiverName);
        masterDataSource.setUrl(ds1DBUrl);
        masterDataSource.setUsername(ds1DBUser);
        masterDataSource.setPassword(ds1DBPassword);

        Map<Object,Object> map = new HashMap<>();
        map.put(DbType.DB0.getDbName(), defaultDataSource);
        map.put(DbType.DB1.getDbName(), masterDataSource);
        dynamicDataSource.setTargetDataSources(map);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:mapper/*.xml"));
        return bean.getObject();

    }

    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
