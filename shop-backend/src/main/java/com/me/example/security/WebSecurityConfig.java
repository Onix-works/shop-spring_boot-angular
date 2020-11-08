package com.me.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.me.example.security.oauth2.CustomOAuth2UserService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomOAuth2UserService oauth2Service;
	
	@Autowired
    private UserDetailsService myUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.cors()
          .and()
            .csrf()//.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .disable()
            //.and()
            .authorizeRequests()
              .antMatchers( "/h2-console/**", "/console/**","/api/register", "/products/**", "/api/image/**", "/*", "/login/**").permitAll()
              .anyRequest().authenticated()
          .and()
          
          .headers().frameOptions().disable()
          .and()
      	    .httpBasic()
      	  .and()
      	    .logout()
      	    .logoutUrl("/api/logout")
      	  .and()
      	    .oauth2Login()		    
      	      .userInfoEndpoint()
			    .userService(oauth2Service)
			  .and()
				.defaultSuccessUrl("http://localhost:8080");

     }  


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(myUserDetailsService);
        return provider;
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
 

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    
}