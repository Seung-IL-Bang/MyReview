package com.web.myreview.repository.user;

import com.web.myreview.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email); // 가입한 적이 있는지 판단하기 위한 메소드


}
