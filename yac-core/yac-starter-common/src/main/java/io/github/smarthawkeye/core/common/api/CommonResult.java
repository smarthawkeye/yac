package io.github.smarthawkeye.core.common.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 统一响应消息报文
 *
 * @param <T> 　T对象
 * @author xiaoya
 * @link https://github.com/an0701/ya-java
 * @since 0.1.0
 */
@ApiModel(value = "统一响应消息报文")
public class CommonResult<T> implements Serializable {
	@ApiModelProperty(value = "状态码")
	private int code;
	@ApiModelProperty(value = "状态信息")
	private String message;
	@ApiModelProperty(value = "返回结果")
	private T data;

	protected CommonResult() {
	}

	protected CommonResult(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	/**
	 * 成功返回结果
	 *
	 * @param data 获取的数据
	 */
	public static <T> CommonResult<T> success(T data) {
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
	}
	/**
	 * 成功返回结果
	 */
	public static <T> CommonResult<T> success() {
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
	}

	/**
	 * 成功返回结果
	 *
	 * @param data 获取的数据
	 * @param  message 提示信息
	 */
	public static <T> CommonResult<T> success(T data, String message) {
		return new CommonResult<T>(ResultCode.SUCCESS.getCode(), message, data);
	}

	/**
	 * 失败返回结果
	 * @param message 提示信息
	 */
	public static <T> CommonResult<T> failed(String message) {
		return new CommonResult<T>(ResultCode.ERR_FAIL.getCode(), message, null);
	}

	/**
	 * 失败返回结果
	 */
	public static <T> CommonResult<T> failed() {
		return failed(ResultCode.ERR_FAIL.getMessage());
	}

	/**
	 * 失败返回结果
	 */
	public static <T> CommonResult<T> failed(ResultCode resultCode) {
		return new CommonResult<T>(resultCode.getCode(),resultCode.getMessage(), null);
	}

	/**
	 * 未登录返回结果
	 */
	public static <T> CommonResult<T> unauthorized(T data) {
		return new CommonResult<T>(ResultCode.TOKEN_NULL_EXPIRE_ERROR.getCode(), ResultCode.TOKEN_NULL_EXPIRE_ERROR.getMessage(), data);
	}

	/**
	 * 未授权返回结果
	 */
	public static <T> CommonResult<T> forbidden(T data) {
		return new CommonResult<T>(ResultCode.NO_AUTHENTICATION.getCode(), ResultCode.NO_AUTHENTICATION.getMessage(), data);
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
