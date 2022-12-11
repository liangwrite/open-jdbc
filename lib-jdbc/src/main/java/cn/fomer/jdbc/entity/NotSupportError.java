/**
 * 
 */
package cn.fomer.jdbc.entity;

/**
 * 2022-03-11
 * 
 */
public class NotSupportError extends RuntimeException {
	
	public NotSupportError(String msg) {
		super(msg);
	}

	public static NotSupportError throwError(String msg) throws RuntimeException{
		return new NotSupportError(msg);
	}
}
