package com.adoptapet.adotapet;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Api Adote um pet", version = "1", description = "api para o teste adote um pete"))
public class AdotapetApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdotapetApplication.class, args);
	}

}
