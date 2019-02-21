package com.bestwehotel.shiro.jwt.model.result;


/**
 * 返回状态码
 * @author lingjw
 * @version 创建时间：2018年03月07日 下午4:12:50
 *
 */
public class MsgCode {

	private String msgCode;
	private String message;


	/**
	通用的错误码
	 */
	public static MsgCode SUCCESS = new MsgCode("100", "请求成功");
	public static MsgCode SYSTEM_ERROR= new MsgCode("500", "系统繁忙，请稍后再试");
	public static MsgCode UNAUTHORIZED = new MsgCode("401", "未登录");
	public static MsgCode TOKEN_EXPIRE = new MsgCode("307", "token过期");
	public static MsgCode VAILD_TOKEN_FAIL = new MsgCode("309", "无效token");


	private MsgCode( ) {
	}

	public MsgCode(String msgCode, String message) {
		this.msgCode = msgCode;
		this.message = message;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MsgCode fillArgs(Object... args) {
		String code = this.msgCode;
		String message = String.format(this.message, args);
		return new MsgCode(code, message);
	}

	@Override
	public String toString() {
		return "MsgCode{" +
				"msgCode='" + msgCode + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}
