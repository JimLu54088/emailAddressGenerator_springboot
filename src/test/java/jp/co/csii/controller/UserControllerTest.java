package jp.co.csii.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import jp.co.csii.entity.Emailgenerator;
import jp.co.csii.repository.UserRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@Spy
	@InjectMocks
	private UserController controller;

	@Mock
	private UserRepository userRepository;

	@Test
	public void success_generateEmail() {
		Emailgenerator emailgenerator = new Emailgenerator();

		emailgenerator.setFirstName("kk");
		emailgenerator.setLastName("ioio");

		ResponseEntity<String> response = controller.generateEmail(emailgenerator);

		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Generated Email: kk-ioio@csii.co.jp", response.getBody());

	}


	@Test
	public void test_generateEmail_withExistingUser_firstTime() {
		// 模擬已經存在相同email的情況

		Emailgenerator emailgenerator = new Emailgenerator();

		emailgenerator.setFirstName("kk");
		emailgenerator.setLastName("ioio");

		Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new Emailgenerator()))
				.thenReturn(Optional.empty());

		ResponseEntity<String> response = controller.generateEmail(emailgenerator);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Generated Email: kk-ioio01@csii.co.jp", response.getBody());
	}

	@Test
	public void test_generateEmail_withExistingUser_secondTime() {
		// 模擬已經存在相同email的情況

		Emailgenerator emailgenerator = new Emailgenerator();

		emailgenerator.setFirstName("kk");
		emailgenerator.setLastName("ioio");

		Mockito.when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new Emailgenerator()))
				.thenReturn(Optional.of(new Emailgenerator())).thenReturn(Optional.empty());

		ResponseEntity<String> response = controller.generateEmail(emailgenerator);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals("Generated Email: kk-ioio02@csii.co.jp", response.getBody());
	}

}
