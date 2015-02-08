package com.silu.dinner.database.dao;

import com.silu.dinner.bean.Location;
import com.silu.dinner.database.DataSource;
import org.apache.ibatis.annotations.Param;

/**
 * Created by silu on 15-2-8.
 */
@DataSource("mysql001.dinner")
public interface LocationDAO {
    public int create(@Param("location") Location location,@Param("userId") String userId);

    public Location get(@Param("userId") String userId);
    
    public int update(@Param("userId") String userId,@Param("dimension") double dimension,@Param("longitude") double longitude);

}
