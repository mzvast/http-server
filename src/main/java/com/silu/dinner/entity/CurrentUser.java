package com.silu.dinner.entity;

import com.silu.dinner.bean.User;

/**
 * 记录当前登录用户，通过ThreadLocal来保存
 */
public class CurrentUser {
    private static ThreadLocal<User> holder = new InheritableThreadLocal<User>();

    public static void clearUser() {
        holder.set(null);
    }

    public static User user() {
        return holder.get();
    }

    public static void user(final User user) {
        holder.set(user);
    }
}
