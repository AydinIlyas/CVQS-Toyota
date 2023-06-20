package com.toyota.verificationauthorizationservice;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

class VerificationAuthorizationServiceApplicationTests {

	@Test
	void contextLoads() {
		SpringApplication springApplicationMock = Mockito.mock(SpringApplication.class);
		VerificationAuthorizationServiceApplication.main(new String[0]);
		Mockito.verify(springApplicationMock).run(VerificationAuthorizationServiceApplication.class, new String[0]);
	}

}
