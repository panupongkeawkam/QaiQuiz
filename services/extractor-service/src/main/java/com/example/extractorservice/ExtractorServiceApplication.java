package com.example.extractorservice;

import com.example.extractorservice.service.FilterServiceImpl;
import com.example.extractorservice.service.SearchServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
public class ExtractorServiceApplication {

	private static Server server;

	public static void main(String[] args) {
		server = ServerBuilder
				.forPort(50052)
				.addService(new FilterServiceImpl())
				.addService(new SearchServiceImpl())
				.build();

		try {
			server.start();
			SpringApplication.run(ExtractorServiceApplication.class, args);
			System.out.println("Server Started at port 50052");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(
				() -> {
					System.out.println("Shutdown Hooked");
					server.shutdown();
				}
		));

		try {
			server.awaitTermination();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
