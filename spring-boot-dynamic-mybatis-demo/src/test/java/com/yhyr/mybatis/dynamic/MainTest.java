package com.yhyr.mybatis.dynamic;

/**
 * @description: 测试
 * @author: zhenqiang.zhan
 * @create: 2019-04-30 16:37
 **/
public class MainTest {

    private static final ThreadLocal<User> userContext = new ThreadLocal<>();

    public static void setUser(User user) {
        userContext.set(user);
    }

    public static User getUser() {
        User user = userContext.get();
        return user;
    }

    public static void main(String[] args) {
        System.out.println(getUser());
        User user = new User(1, "admin");
        setUser(user);
        User contextUser = getUser();
        System.out.println(contextUser);
    }
}
