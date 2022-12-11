package cn.fomer.jdbc.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ServletComponentScan
public class Service1Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Service1Application.class, args);
		Environment environment = context.getBean(Environment.class);
		String server_port = environment.getProperty("server.port");
		System.out.println("启动成功~"+server_port);
	}
		
}
