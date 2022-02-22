package org.brewchain.sdk.model;


public class ChainResult {

    /**
     * 1成功，其他失败
     */
    private int code;
    private String message;
    private Object data;

    public ChainResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ChainResult(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public ChainResult(int code, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    @Override
    public String toString() {
        return "ChainResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
