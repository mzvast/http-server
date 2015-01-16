package com.silu.dinner.util;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by silu on 15-1-15.
 */
@Service
public class SessionUtil {
    public String newUserId(){
       return UUID.randomUUID().toString().replace("-","");
    }
    
    public String  newTokenId(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
