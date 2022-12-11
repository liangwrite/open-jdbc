/**
 * 
 */
package cn.fomer.jdbc.entity;

/**
 * 2021-04-14
 * 
 */
public class Error extends RuntimeException
{
	public static void throwError(String message)
	{
		throw new RuntimeException(message);
	}
	
	public static void throwError(Exception e)
	{
		throw new RuntimeException(e.getMessage());
	}

}
