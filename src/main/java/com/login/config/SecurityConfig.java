package com.login.config;


import com.login.config.auth.PrincipalOauth2UserService;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.SecurityFilterChain;


// 소셜 로그인 방식
// (1) 1. 코드받기(인증) 2. 엑세스토큰(권한) 3. 사용자 프로필 정보를 가져오기 4. 받아온 정보를 토대로 회원가입을 자동으로 시키기도함
// (2) 4-2 받아온 정보가 부족할때 ex) 집 주소, 고객 등급 등 추가적인 회원가입 창을 가지고 추가적으로 회원가입을 해야 함.
@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorized와 postAuthorize 어노테이션 활성화
public class SecurityConfig{

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()    // 글로벌로 권한 설정
                .antMatchers("/user/**").authenticated()    // 인증만 되면 들어갈 수 있는 주소
                .antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                // .loginPage("/loginForm") -> localhost:8080/loginForm을 통해서 로그인을 해서 성공하면
                // .defaultSuccessUrl("/"); -> 디폴트 페이지로 보내주고
                // 어떤 특정 페이지를 요청해서 로그인을 하게되면 해당 특정 페이지로 보내줄게 (권한이 있다면 ex. 유저가 매니저페이지로 가면 403 권한이 없다고 나옴, admin도)
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")  // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행해줌. = 컨트롤러에 로그인을 안만들어도됨
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")       // google 로그인이 완료된 후의 후처리가 필요함 Tip. 코드X, (엑세스토큰 + 사용자프로필정보 O)
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }
}