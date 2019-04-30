package com.yhyr.mybatis.dynamic.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YHYR on 2017-12-25
 * @author zzhan
 */

public class DynamicDataSource extends AbstractRoutingDataSource {
    private static DynamicDataSource instance;
    private static byte[] lock=new byte[0];
    private static Map<Object,Object> dataSourceMap=new HashMap<Object, Object>();

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceMap.putAll(targetDataSources);
        // 必须添加该句，否则新添加数据源无法识别到
        super.afterPropertiesSet();
    }

    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    public static synchronized DynamicDataSource getInstance(){
        if(instance==null){
            synchronized (lock){
                if(instance==null){
                    instance=new DynamicDataSource();
                }
            }
        }
        return instance;
    }

    /**
     * 该方法决定具体使用哪个数据源
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDBType();
    }
}
