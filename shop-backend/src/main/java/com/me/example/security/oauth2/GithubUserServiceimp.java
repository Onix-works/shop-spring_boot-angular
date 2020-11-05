package com.me.example.security.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.me.example.cart.InCartProduct;
import com.me.example.exceptions.EntityAlreadyExistException;
import com.me.example.exceptions.EntityDoesNotExistException;
import com.me.example.user.MyUser;
import com.me.example.user.MyUserRepository;
import com.me.example.user.MyUserService;

@Service("githubUserService")
public class GithubUserServiceimp implements GithubUserService {
	
	@Autowired
	GithubUserRepository repository;


	@Override
	public List<GithubUser> findAll() {
		return repository.findAll();
	}

	@Override
	public GithubUser findById(Long id) {
		if (repository.findById(id).isPresent()){   
			return repository.findById(id).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No account with such id.");
		 }
	}

	@Override
	public GithubUser findByLogin(String email) {
		if (repository.findByLogin(email).isPresent()){   
			return repository.findByLogin(email).get();
        }
	 else {
		 throw new EntityDoesNotExistException("No account with such login.");
		 }
	}

	@Override
	public GithubUser save(GithubUser user) {
		 if (repository.findByLogin(user.getLogin()).isPresent()){   
	            throw new EntityAlreadyExistException(
	              "There is an account with that email address:  "
	              + user.getLogin());
	        }
		 else {
			 
			 return repository.save(user);
		 }
		 
	}

	@Override
	public GithubUser update(GithubUser user) {
		if (user.getId()!=null&&repository.findById(user.getId()).isPresent()) 
			return repository.save(user);
		else {
			if (user.getLogin()!=null&&repository.findByLogin(user.getLogin()).isPresent()) {
				user.setId(repository.findByLogin(user.getLogin()).get().getId());
				return repository.save(user);
			}
		   }
		
		throw new EntityDoesNotExistException("No user id to update");
		
	}
	
	@Override
	public boolean loginExists(String login) {
		if (repository.findByLogin(login).isPresent()) {
			return true;
		}
		else 
			return false;		
	}
	
	@Override
	public boolean idExists(Long id) {
	if (repository.findById(id).isPresent()) {
		return true;
	}
	else 
		return false;
	}
}