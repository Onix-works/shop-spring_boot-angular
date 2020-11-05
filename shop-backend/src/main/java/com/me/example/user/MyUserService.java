package com.me.example.user;

import java.util.List;

public interface MyUserService {
	List<MyUser> findAll();
	MyUser findById(Long id);
	MyUser findByEmail(String email);
	MyUser save(MyUser user);
	MyUser update(MyUser user);

}
