package com.silu.dinner.service;

import com.silu.dinner.bean.User;
import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.database.dao.UserDAO;
import com.silu.dinner.exception.ServerException;
import com.silu.dinner.util.SessionUtil;
import com.silu.dinner.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        if(null == user.getAccount() || "".equals(user.getAccount().trim())){
            LOGGER.warn("fails to create user");
            throw new ServerException(StatusCode.REQ_PARAMETER_WRONG,"account should not be null.");
        }

        User isExistUser = userDAO.getByAccount(user.getAccount());

        if(null != isExistUser){
            LOGGER.warn("user：{} exits.",user);
            throw new ServerException(StatusCode.USER_EXIST,"account exists.");
        }

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

    public void update(User user) throws ServerException{
        try {
           userDAO.update(user);
        }catch (Exception e){
            LOGGER.warn("fails to create user",e);
            throw new ServerException(StatusCode.USER_QUERY_FAILED,"fails to update user");
        }
    }

    public String login(String account,String password){
        String newTokenId = sessionUtil.newTokenId();
        User user = null;
        try{
           user = userDAO.getByAccount(account);
        }
        catch (Exception e){
            LOGGER.warn("fails to create user",e);
            throw new ServerException(StatusCode.USER_QUERY_FAILED,"fails to update user");
        }

        if(null == user){
            LOGGER.warn("user:{} no exist.",account);
            throw new ServerException(StatusCode.USER_NO_EXIST,"fails to update user");
        }

        if(user.getPassword() != null && !user.getPassword().equals(password)){
            throw new ServerException(StatusCode.USER_ERROR,"account or password is wrong.");
        }

        User updateSessionUser = new User();
        updateSessionUser.setTokenId(newTokenId);
        updateSessionUser.setUserId(user.getUserId());

        try{
            userDAO.update(updateSessionUser);
        }
        catch (Exception e){
            LOGGER.warn("fails to create user",e);
            throw new ServerException(StatusCode.INNER_ERROR,"fails to create token");
        }


        return newTokenId;
    }
}
