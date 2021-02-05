package com.nisum.springwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class SpringWebFluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebFluxApplication.class, args);
	}

}
