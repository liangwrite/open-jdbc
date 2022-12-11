package cn.fomer.common.web.page;

import java.io.Serializable;

/**
 * @author Liang 20181111 用于封装分页请求参数
 *
 */
public class QueryPageBean implements Serializable
{
	Integer pageSize;
	Integer pageNum;
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
}
