package com.adobe.bookstore;

import com.adobe.bookstore.model.Product;
import com.adobe.bookstore.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@SpringBootApplication
public class BookstoreApplication {


	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ProductService productService) {
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<List<Product>> typeReference = new TypeReference<>() {
			};
			InputStream inputStream = TypeReference.class.getResourceAsStream("/json/stock.json");
			try {
				List<Product> products = mapper.readValue(inputStream, typeReference);
				for (Product p : products)
					productService.save(p);
				System.out.println("Done saving products into DB");
			} catch (IOException e) {
				System.out.println("Unable to save products: " + e.getMessage());
			}
		};
	}
}
