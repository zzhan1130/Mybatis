package com.yhyr.mybatis.dynamic.enums;

/**
 * @description: TODO
 * @author: zhenqiang.zhan
 * @create: 2019-04-30 15:52
 **/
public enum DbType {

    DB0("db0"),
    DB1("db1"),
    DB2("db2");

    private String dbName;

    DbType(String dbName) {
        this.dbName = dbName;
    }

    public String getDbName() {
        return dbName;
    }
}
