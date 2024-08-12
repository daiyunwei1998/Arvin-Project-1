package com.stylish.stylish;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log4j2
public class StylishApplication {

	public static void main(String[] args) {

		SpringApplication.run(StylishApplication.class, args);
	}

}
