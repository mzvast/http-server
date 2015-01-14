package com.silu.dinner.bean;

import com.silu.dinner.constant.StatusCode;
import com.silu.dinner.constant.StatusDesc;

/**
 * Created by silu on 15-1-14.
 */
public class HttpResp {
    private String status;
    private String desc;
    private Object body;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static HttpResp newSuccessResp() {
        HttpResp success = new HttpResp();
        success.status = StatusCode.SUCCESS;
        success.desc = StatusDesc.SUCCESS;
        return success;
    }

    @Override
    public String toString() {
        return "HttpResp{" +
                "status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", body=" + body +
                '}';
    }
}
