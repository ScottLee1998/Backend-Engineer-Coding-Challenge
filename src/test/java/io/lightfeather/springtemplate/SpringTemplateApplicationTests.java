package io.lightfeather.springtemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class SpringTemplateApplicationTests {

	@Spy
	public static Application classUnderTest;

	@Test
	void contextLoads() {
	}

	@Test
	void testGetEndpointSuccess() {
		ReflectionTestUtils.setField(classUnderTest, "source", "https://o3m5qixdng.execute-api.us-east-1.amazonaws.com/api/managers");
		assertEquals(HttpStatus.OK, classUnderTest.getSupervisors().getStatusCode());
		ReflectionTestUtils.setField(classUnderTest, "source", "invalidUri");
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, classUnderTest.getSupervisors().getStatusCode());
	}

	@Test
	void testPostEndpointRequiresMinimalData() {
		assertEquals(HttpStatus.OK, classUnderTest.createUser("John", "Doe", null, null, "Smith").getStatusCode());
		assertEquals(HttpStatus.OK, classUnderTest.createUser("John", "Doe", "validEmail", "validPhone", "Smith").getStatusCode());
		assertEquals(HttpStatus.BAD_REQUEST, classUnderTest.createUser(null, null, null, null, null).getStatusCode());
		assertEquals(HttpStatus.BAD_REQUEST, classUnderTest.createUser("", "", null, null, "").getStatusCode());
	}
}
