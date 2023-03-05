package com.umesh.config;

import com.umesh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/showPageForWriting/**").hasAnyRole("Admin", "Author")
                .antMatchers("/updatePost/**").hasAnyRole("Admin", "Author")
                .antMatchers("/deleteStory").hasAnyRole("Admin", "Author")
                .antMatchers("/updateComment/**").hasAnyRole("Admin", "Author")
                .antMatchers("/deleteComment/**").hasAnyRole("Admin", "Author")
                .antMatchers(HttpMethod.GET,"/api/**").permitAll()
                .antMatchers(HttpMethod.POST,"/api/post").hasAnyRole("Admin", "Author")
                .antMatchers(HttpMethod.PUT,"/api/post").hasAnyRole("Admin", "Author")
                .antMatchers(HttpMethod.DELETE,"/api/post").hasAnyRole("Admin", "Author")
                .antMatchers(HttpMethod.POST,"/api/comment").permitAll()
                .antMatchers(HttpMethod.PUT,"/api/comment").hasAnyRole("Admin", "Author")
                .antMatchers(HttpMethod.DELETE,"/api/comment").hasAnyRole("Admin", "Author")
                .and()
                .httpBasic()
                .and()
                .formLogin()
                .loginPage("/showLoginPage")
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
                .and()
                .logout().permitAll();
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        return auth;
    }
}
