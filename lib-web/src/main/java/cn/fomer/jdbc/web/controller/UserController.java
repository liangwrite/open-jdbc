package cn.fomer.jdbc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

//@RestController
@Controller
@RequestMapping("/user")
public class UserController {
	


	@RequestMapping("/info")
	@ResponseBody
	public Object info()
	{
		return "info";
	}
	
}
