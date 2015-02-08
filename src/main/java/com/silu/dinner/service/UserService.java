package com.silu.dinner.service;

import com.silu.dinner.bean.User;
import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.database.dao.UserDAO;
import com.silu.dinner.exception.ServerException;
import com.silu.dinner.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Created by silu on 15-1-15.
 */
@Service
public class UserService{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private SessionUtil sessionUtil;

    public User createUser(User user) throws ServerException{
        user.setUserId(sessionUtil.newUserId());
        user.setTokenId(sessionUtil.newTokenId());
        try {
            userDAO.create(user);
        }catch (Exception e){
            LOGGER.warn("fails to create user",e);
            throw new ServerException(StatusCode.USER_CREATE_FAILED,"fails to create user");
        }
        return user;
    }

    public User getUser(String userId) throws ServerException{
        User user = null;
        try {
            user = userDAO.get(userId);
        }catch (Exception e){
            LOGGER.warn("fails to create user",e);
            throw new ServerException(StatusCode.USER_QUERY_FAILED,"fails to query user");
        }
        return user;
    }
    
    public User authorize(String session_id) throws ServerException{
        User user = null;
        try {
            user = userDAO.authorize(session_id);
        }catch (Exception e){
            LOGGER.warn("fails to create user",e);
            throw new ServerException(StatusCode.USER_QUERY_FAILED,"fails to query user");
        }
        return user;
    }
}
