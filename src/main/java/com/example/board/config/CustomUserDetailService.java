package com.example.board.config;

import com.example.board.domain.Member;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
   로그인 시(/login) Spring Security가 호출하는 서비스
   loginId(username)로 회원 정볼르 찾아서 userDetails 타입의 객체로 리턴한다.
   패스워드 비교, 세션 저장은 Spring Security가 알아서 처리한다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("로그인 요청: {}", username);
		Member member = memberRepository.findByLoginId(username)
				.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 사용자: " + username));
		return new CustomUserDetails(member);
	}
}
