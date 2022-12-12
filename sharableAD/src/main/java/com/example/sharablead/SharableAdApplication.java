package com.example.sharablead;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class SharableAdApplication {

	private ThreadPoolTaskScheduler taskScheduler = null;

	//initialize thread pool for cron job
	@Bean
	public ThreadPoolTaskScheduler initTaskScheduler() {
		if (taskScheduler != null) {
			return taskScheduler;
		}
		taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(20);
		return taskScheduler;
	}

	public static void main(String[] args) {
		//set default timezone as UTC, why not GMT, cause UTC is exacter than GMT
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(SharableAdApplication.class, args);
	}

}
