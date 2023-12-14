package jp.co.csii.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.csii.entity.Emailgenerator;

@Repository
public interface UserRepository extends JpaRepository<Emailgenerator, Long> {
	  Optional<Emailgenerator> findByEmail(String email);
}