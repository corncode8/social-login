package com.login.repository;
import java.util.Optional;

import com.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// DAO
// 자동으로 Bean으로 등록이 된다. @Repository 생략 가능하다.
// CRUD 함수를 JpaRepository가 들고 있음.
// @Repository라는 어노테이션이 없어도 됨. 이유는 JpaRepository를 상속했기 때문
public interface UserRepository extends JpaRepository<User, Integer>{
    // SELECT * FROM user WHERE username = username
    public User findByUsername(String username);    // findByUsername = Jpa Query method

    // SELECT * FROM user WHERE provider = ?1 and providerId = ?2
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
