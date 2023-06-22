package com.prostudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "com.prostudy.config")
public class StudyProjectBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyProjectBackendApplication.class, args);
	}

}
