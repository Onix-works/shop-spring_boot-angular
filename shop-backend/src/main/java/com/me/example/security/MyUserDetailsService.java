package com.me.example.security;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.me.example.user.MyUser;
import com.me.example.user.MyUserRepository;


@Service("myUserDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService  {
	
	private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
  
    @Autowired
    private MyUserRepository userRepository;
    // 
   
    public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
  
        MyUser user = userRepository.findByEmail(email).get();
        if (user == null) {
            throw new UsernameNotFoundException(
              "No user found with username: "+ email);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        User secUser = new User
                (user.getEmail(), 
                        user.getPassword(), enabled, accountNonExpired, 
                        credentialsNonExpired, accountNonLocked, 
                        getAuthorities(user.getRoles()));
        logger.info("Authorizing user:" + secUser.toString());
        
        return  secUser;
    }
     
    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}

