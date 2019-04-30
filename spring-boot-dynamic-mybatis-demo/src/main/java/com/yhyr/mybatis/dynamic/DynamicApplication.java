package com.yhyr.mybatis.dynamic;

import com.alibaba.druid.pool.DruidDataSource;
import com.yhyr.mybatis.dynamic.datasource.DataSourceContextHolder;
import com.yhyr.mybatis.dynamic.datasource.DynamicDataSource;
import com.yhyr.mybatis.dynamic.domain.DBInfo;
import com.yhyr.mybatis.dynamic.domain.StudentInfo;
import com.yhyr.mybatis.dynamic.domain.UserInfo;
import com.yhyr.mybatis.dynamic.enums.DbType;
import com.yhyr.mybatis.dynamic.services.DBService;
import com.yhyr.mybatis.dynamic.services.StudentService;
import com.yhyr.mybatis.dynamic.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Map;

/**
 * Created by YHYR on 2017-12-25
 */

@SpringBootApplication
public class DynamicApplication implements CommandLineRunner {
	@Autowired
	UserService userService;

	@Autowired
	DBService dbService;

	@Autowired
	StudentService studentService;

	public static void main(String[] args) {
		SpringApplication.run(DynamicApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		/**
		 * 从数据源0获取用户信息
		 */
		DataSourceContextHolder.setDBType(DbType.DB1.getDbName());
		List<UserInfo> userInfoList = userService.getUserInfo();
		userInfoList.stream().forEach(userInfo -> System.out.println("name is : " + userInfo.getName() + "; sex is : " + userInfo.getSex() + "; age is : " + userInfo.getAge()));

		/**
		 * 从数据源1获取db信息
		 */
		DataSourceContextHolder.setDBType(DbType.DB0.getDbName());
		int primayrId = 1;
		DBInfo dbInfo = dbService.getDBInfoByprimayrId(primayrId);
		System.out.println("dbName is -> " + dbInfo.getDbName() + "; dbIP is  -> " + dbInfo.getDbIp() + "; dbUser is  -> " + dbInfo.getDbUser() + "; dbPasswd is -> " + dbInfo.getDbPasswd());


		/**
		 * 创建数据源
		 */
		DruidDataSource dynamicDataSource = new DruidDataSource();
		dynamicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dynamicDataSource.setUrl("jdbc:mysql://192.168.62.244:3306/ds2?useSSL=false&characterEncoding=utf-8");
		dynamicDataSource.setUsername("root");
		dynamicDataSource.setPassword("YYJNo$QsaaSjgb8U3JoigB");

		Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
		dataSourceMap.put(DbType.DB2.getDbName(), dynamicDataSource);
		DynamicDataSource.getInstance().setTargetDataSources(dataSourceMap);

		/**
		 * 切换为动态数据源实例，打印学生信息
		 */
		DataSourceContextHolder.setDBType(DbType.DB2.getDbName());
		List<StudentInfo> studentInfoList = studentService.getStudentInfo();
		studentInfoList.stream().forEach(studentInfo -> System.out.println("studentName is : " + studentInfo.getStudentName() + "; className is : " + studentInfo.getClassName() + "; gradeName is : " + studentInfo.getGradeName()));

	}
}
