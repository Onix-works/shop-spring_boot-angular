package com.me.example.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me.example.cart.InCartProduct;
import com.me.example.exceptions.EntityAlreadyExistException;
import com.me.example.exceptions.EntityDoesNotExistException;



@Service("myUserService")
public class MyUserServiceImp implements MyUserService {
	
	@Autowired
	MyUserRepository repository;


	@Override
	public List<MyUser> findAll() {
		return repository.findAll();
	}

	@Override
	public MyUser findById(Long id) {
		if (repository.findById(id).isPresent()){   
			return repository.findById(id).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No account with such id.");
		 }
	}

	@Override
	public MyUser findByEmail(String email) {
		if (repository.findByEmail(email).isPresent()){   
			return repository.findByEmail(email).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No account with such email.");
		 }
	}

	@Override
	public MyUser save(MyUser user) {
		 if (repository.findByEmail(user.getEmail()).isPresent()){   
	            throw new EntityAlreadyExistException(
	              "There is an account with that email address:  "
	              + user.getEmail());
	        }
		 else {
			 user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			 user.setRoles(new ArrayList<String>());
			 user.getRoles().add("ROLES_USER");
			 user.setInCartProducts(new ArrayList<InCartProduct>());
			 return repository.save(user);
		 }
		 
	}

	@Override
	public MyUser update(MyUser user) {
		//if (repository.findById(user.getId()).isPresent()){
			return repository.save(user);
		//}
		//else {
		//	throw new EntityDoesNotExistException("No account to update.");
		//}
	}
	
	

}
