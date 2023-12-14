package jp.co.csii.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.csii.entity.Emailgenerator;
import jp.co.csii.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@PostMapping("/generate-email")
	public ResponseEntity<String> generateEmail(@RequestBody Emailgenerator userRequest) {
		String firstName = userRequest.getFirstName();
		String lastName = userRequest.getLastName();

		// 生成初始的邮箱
		String generatedEmail = generateEmail(firstName, lastName, 0);

		// 查找数据库是否存在相同的邮箱
		Optional<Emailgenerator> existingUser = userRepository.findByEmail(generatedEmail);
		if (existingUser.isPresent()) {
			// 如果存在相同的邮箱，从尾数01开始查找可用的邮箱
			int count = 1;
			do {
				generatedEmail = generateEmail(firstName, lastName, count);
				existingUser = userRepository.findByEmail(generatedEmail);
				count++;
			} while (existingUser.isPresent());
		}

		// 创建新用户
		Emailgenerator newUser = new Emailgenerator();
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(generatedEmail);

		userRepository.save(newUser);

		return ResponseEntity.ok("Generated Email: " + generatedEmail);
	}

	// 生成邮箱的方法
	private String generateEmail(String firstName, String lastName, int count) {
		String baseEmail = String.format("%s-%s", firstName.toLowerCase(), lastName.toLowerCase());
		if (count > 0) {
			return String.format("%s%02d@csii.co.jp", baseEmail, count);
		} else {
			return String.format("%s@csii.co.jp", baseEmail);
		}
	}
}
