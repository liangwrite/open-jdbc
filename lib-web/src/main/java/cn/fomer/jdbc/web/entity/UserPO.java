package cn.fomer.jdbc.web.entity;

import javax.annotation.PostConstruct;

public class UserPO {

	public UserPO()
	{
		System.out.println("**** UserPO.UserPO()");
	}
	
	@PostConstruct
	public void init()
	{
		
	}
}
