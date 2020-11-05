package com.me.example.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MyUserRepository extends JpaRepository<MyUser, Long> {
	
	Optional<MyUser> findByEmail(String email);
	
	@Override
    void delete(MyUser user);

}
