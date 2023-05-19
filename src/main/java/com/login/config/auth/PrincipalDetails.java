package com.login.config.auth;


// 시큐리티가 /login주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인을 진행이 만료가 되면 시큐리티 session을 만들어준다 (Security ContextHolder)
// 오브젝트 => Authentication 타입 객체
// Authentication 안에 유저 정보가 있어야됨. 클래스가 정해져 있음
// User 오브젝트타입 => UserDetails 타입 객체

import com.login.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// Security Session => Authentication => UserDetails(PrincipalDetails)타입
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    public User user;   // 콤포지션
    private Map<String, Object> attributes;

    // 일반 로그인
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 해당 유저의 권한을 리턴하는 곳
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // 계정 비밀번호 기간이 지났니
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 계정이 활성화 되어있니
        // 우리 사이트에서 1년동안 로그인을 안하면 휴면 계정으로 하기로 함.
        // model-user에 private Timestamp loginDate 변수 생성 후
        // user.getLoginDate(); -> 현재시간 - 로그인시간이 1년을 초과하면 return 을 false로 하면 됨

        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
