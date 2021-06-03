package com.devin.dev;

import com.devin.dev.sample.BackedLoginService;
import com.devin.dev.security.LoginSuccessHandler;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;

@Configuration
@EnableWebSecurity
public class AppConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .cors().disable()      // cors 비활성화
                .csrf().disable()      // csrf 비활성화
                .authorizeRequests()
                    .antMatchers("/login", "/signUp")
                        .permitAll()
                    .anyRequest()
                    .authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/signIn")
                    .usernameParameter("id")
                    .passwordParameter("pw")
                    .successHandler(new LoginSuccessHandler())
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login");
    }
}
