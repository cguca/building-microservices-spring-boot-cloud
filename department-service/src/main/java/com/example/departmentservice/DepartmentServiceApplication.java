package com.example.departmentservice;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Department Service",
				description = "Department Service REST API Document",
				version = "1.0",
				contact = @Contact(
						name = "Cary",
						email = "com.example",
						url = "com.example"
				),
				license = @License(
						name = "Apache 2.0",
						url = "com.example"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Department Service doc",
				url = "com.example"
		)
)
@SpringBootApplication
public class DepartmentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepartmentServiceApplication.class, args);
	}

}
