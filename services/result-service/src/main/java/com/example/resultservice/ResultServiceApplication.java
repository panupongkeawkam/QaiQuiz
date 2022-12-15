package com.example.resultservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableDiscoveryClient
@CrossOrigin
public class ResultServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResultServiceApplication.class, args);
	}

}
