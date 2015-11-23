package com.liulishuo.autoteller.mvc.models;

/**
 * Created by Administrator on 2015/11/21 0021.
 */
public class ApiResult {

    private int status;

    private String message;

    private Object data;

    public static ApiResult ILLEGAL_ARGS = new ApiResult(-1, "Illegal Argument:");

    public static ApiResult SUCCESS = new ApiResult(0, "Success:");

    public static final int USER_ERR_START = -2048;

    public static final int TRANSACTION_FAILED = USER_ERR_START - 1;

    public ApiResult(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public void appendMessage(String msg) {
        this.message = this.message + msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
