package cn.fomer.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 2019-06-29 打印日志
 * 
 * 
 */
@Component
public class LogServletFilter implements Filter {

	Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		try
		{
			HttpServletRequest request= (HttpServletRequest)servletRequest;
			if(!request.getRequestURI().contains("."))
			{
				String text = RequestPrinter.toText((HttpServletRequest)servletRequest);
				System.out.println(text);
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			log.info("LogFilter.doFilter "+e.toString());
		}
		chain.doFilter(servletRequest, servletResponse);
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
