package com.example.sharablead;

import com.example.sharablead.service.SpecificCronJobService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SharableAdApplicationTests {

	@Autowired
	private SpecificCronJobService specificCronJobService;

	@Test
	void contextLoads() {
		specificCronJobService.calculateActiveScore();
	}

}
