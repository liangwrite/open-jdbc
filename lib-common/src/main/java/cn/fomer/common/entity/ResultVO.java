package cn.fomer.common.entity;

/**
 * 2017-10-22
 * 
 * 
 */
public class ResultVO {
	Boolean success;
	String message;
	Object data;
	
	
	/**
	 * 2018-12-10 成功，并写入数据
	 * 
	 */
	public static ResultVO success(Object data)
	{
		ResultVO resultVO= new ResultVO();
		resultVO.success= true;
		resultVO.data= data;
		
		return resultVO;
	}
	
	
	
	/**
	 * 2018-12-10 失败，并且失败原因
	 * 
	 */
	public static ResultVO error(String message)
	{
		ResultVO resultVO= new ResultVO();
		resultVO.success= false;
		resultVO.message= message;
		
		return resultVO;
	}
	
	
	public Boolean getSuccess() {
		return success;
	}
	
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData() {
		return (T)data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
