package cn.fomer.common.web.page;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.PageInfo;

/**
 * @author Liang 20181111 用于响应分页请求
 *
 */
public class PageListBean implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Long total; //未分页时
	Integer pageSize; //
	Integer pageNum; //	
	Integer pages; //可选（分页数量）	
	List<?> list;

	
	public PageListBean(){}
	
	public PageListBean(PageInfo p)
	{
		this.total= p.getTotal();
		this.pageSize= p.getPageSize();
		this.pageNum= p.getPageNum();
		this.pages= p.getPages();
		this.list= p.getList();
	}

	public Long getTotal() {
		return total;
	}


	public void setTotal(Long total) {
		this.total = total;
	}


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


	public List<?> getList() {
		return list;
	}


	public void setList(List<?> list) {
		this.list = list;
	}

	public Integer getPages() {
		return pages;
	}

	public void setPages(Integer pages) {
		this.pages = pages;
	}
}
