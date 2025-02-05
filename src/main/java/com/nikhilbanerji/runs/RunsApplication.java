package com.nikhilbanerji.runs;

import com.nikhilbanerji.runs.run.Location;
import com.nikhilbanerji.runs.run.Run;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class RunsApplication {
	private static final Logger log = LoggerFactory.getLogger(RunsApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(RunsApplication.class, args);
	}

}
