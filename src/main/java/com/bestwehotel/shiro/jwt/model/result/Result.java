package com.bestwehotel.shiro.jwt.model.result;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class Result<T> implements Serializable {

    protected static final long serialVersionUID = -1586118647101027089L;
    
    protected static final String SUCCESS_MSG="操作成功";
    
    protected static final String FAIL_MSG="操作失败";
    

    /**
     * 返回代码
     */
    @ApiModelProperty(value = "响应码， 100-成功，200:系统错误,JSON转换出错，300:请求参数有误，500系统繁忙,请稍后再试其他失败。如有特殊情况可根据具体接口制定", required = true)
    private String resultCode = MsgCode.SYSTEM_ERROR.getMsgCode();

    /**
     * 信息
     */
    @ApiModelProperty(value = "响应描述", required = true)
    private String resultMsg;

    /**
     * 对象
     */
    @ApiModelProperty(value = "业务数据", required = true)
    private T data;

    public Result() {
    }
    public Result(String resultCode){
    	this(resultCode,null,resultCode.equals(MsgCode.SUCCESS.getMsgCode())?SUCCESS_MSG:FAIL_MSG);
    }
    public Result(String resultCode, T data){
    	this(resultCode,data,resultCode.equals(MsgCode.SUCCESS.getMsgCode())?SUCCESS_MSG:FAIL_MSG);
    }
    public Result(String resultCode, T data, String msg){
    	this.setResultCode(resultCode);
    	this.setResultMsg(msg);
    	this.setData(data);
    }
    
    public boolean isSuccess() {
    	if(MsgCode.SUCCESS.getMsgCode().equals(this.getResultCode())) {
    		return true;
    	}
        return false;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String code) {
        this.resultCode = code;
        if(code == MsgCode.SUCCESS.getMsgCode()){
            this.resultMsg=SUCCESS_MSG;
        }
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String message) {
        this.resultMsg = message;
    }

    public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public Result(MsgCode msgCode) {
        this.resultCode = msgCode.getMsgCode();
        this.resultMsg = msgCode.getMessage();
    }

    public static<T> Result<T> error(MsgCode msgCode){

        return  new Result<T>(msgCode);
    }

    public static<T> Result<T> error(){
        return  new Result<T>(MsgCode.SYSTEM_ERROR);
    }

    public static<T> Result<T> error(String errorCode,String errorMsg){
        return  new Result<T>(errorCode,null,errorMsg);
    }

    public static<T> Result<T> error(String errorMsg){
        return  new Result<T>(MsgCode.SYSTEM_ERROR.getMsgCode(),null,errorMsg);
    }

    public void setResult(MsgCode msgCode) {

        this.resultCode = msgCode.getMsgCode();
        this.resultMsg = msgCode.getMessage();
    }

    public static<T> Result<T> success() {
        return  new Result<T>(MsgCode.SUCCESS);
    }

    public static<T> Result<T> success(MsgCode msgCode) {
        return  new Result<T>(msgCode);
    }

    public static<T> Result<T> success(T data) {
        return  new Result<T>(MsgCode.SUCCESS.getMsgCode(),data);
    }
}
