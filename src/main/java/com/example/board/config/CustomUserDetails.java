package com.example.board.config;

import com.example.board.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/*
   Spring Security가 인증에 사용하는 사용자 정보
   우리가 만든 Member를 감싸서 UserDetails 규격에 맞춘다.
 */
@Getter
public class CustomUserDetails implements UserDetails {
	private final Member member;
	
	public CustomUserDetails(Member member) {
		this.member = member;
	}
	
	// 권한 목록을 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(member.getRole().name()));
	}
	
	// 패스워드 리턴
	@Override
	public String getPassword() {
		return member.getPassword();
	}
	
	// 로그인 아이디 리턴
	@Override
	public String getUsername() {
		return member.getLoginId();
	}
	
	// 사용자 계정이 만료되지 않았는지 유무를 리턴
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	// 사용자 계정이 잠기지 않았는지 유무
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	// 사용자 계정의 인증이 만료되지 않았는지 유무
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// 사용자 계정이 사용 가능한지 유무
	@Override
	public boolean isEnabled() {
		return true;
	}
}