/*
+--------------------------------------------------------------------------
|   mtons [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.ruoyi.cms.oss.exception;

/**
 * 
 * @author langhsu
 *
 */
public class StorageException extends RuntimeException {
	private static final long serialVersionUID = -7443213283905815106L;
	private int code;

	public StorageException() {
	}
	
	/**
	 * StorageException
	 * @param code 错误代码
	 */
	public StorageException(int code) {
		super("code=" + code);
		this.code = code;
	}

	/**
	 * StorageException
	 * @param message 错误消息
	 */
	public StorageException(String message) {
		super(message);
	}

	/**
	 * StorageException
	 * @param cause 捕获的异常
	 */
	public StorageException(Throwable cause) {
		super(cause);
	}

	/**
	 * StorageException
	 * @param message 错误消息
	 * @param cause 捕获的异常
	 */
	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * StorageException
	 * @param code 错误代码
	 * @param message 错误消息
	 */
	public StorageException(int code, String message) {
		super(message);
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
