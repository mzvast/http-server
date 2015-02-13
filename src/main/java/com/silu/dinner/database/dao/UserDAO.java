package com.silu.dinner.database.dao;

import com.silu.dinner.bean.User;
import com.silu.dinner.database.DataSource;
import org.apache.ibatis.annotations.Param;

/**
 * Created by silu on 15-1-15.
 */
@DataSource("mysql001.dinner")
public interface UserDAO {
    public int create(@Param("user") User user);

    public User get(@Param("userId") String userId);

    public int delete(@Param("userId") String id);

    public User authorize(@Param("sessionId") String sessionId);

    public int update(@Param("user") User user);

    public User getByAccount(@Param("account") String account);

}
