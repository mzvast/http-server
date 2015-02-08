package com.silu.dinner.service;

import com.silu.dinner.bean.Location;
import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.database.dao.LocationDAO;
import com.silu.dinner.exception.ServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by silu on 15-2-8.
 */
@Service
public class LocationService {
    private Logger LOGGER = LoggerFactory.getLogger(LocationService.class);
    @Autowired
    private LocationDAO locationDAO;
    
   public void uploadLocation(Location location,String userId) throws ServerException{
       Assert.notNull(location);
       Assert.notNull(userId);
       try{
           Location existLoation = locationDAO.get(userId);
           if(null != existLoation){
               locationDAO.update(userId,location.getDimension(),location.getLongitude());
           }
           else {
               locationDAO.create(location,userId);
           }
       }catch (Exception e){
           LOGGER.error("fail to inser location.",e);
           throw new ServerException(StatusCode.INNER_ERROR,"fails to save user location");
       }
   }       
}