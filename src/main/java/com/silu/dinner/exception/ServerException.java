package com.silu.dinner.exception;

/**
 * Created by silu on 15-1-14.
 */
public class ServerException extends RuntimeException {
    private String code;
    private String desc;
    
    public ServerException(String code,String desc){
        super(desc);
        this.code = code;
        this.desc = desc;
        
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return "ServerException{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }
}

