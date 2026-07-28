package com.example.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 로그인 처리, 세션 관리, 인가 검사, 비밀번호 검증 등을 프레임워크가 대신한다.
@Configuration
@EnableWebSecurity   // 시큐리티 설정을 활성화
public class SecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// CSRF 토큰 비활성화 설정
				// Cross-Site Request Forgery : 사용자의 의도와 관계없이 공격자가
				// 다른 웹사이트를 통해 사용자의 브라우저로 특정 요청을 보내는 공격 방식
				.csrf(csrf -> csrf.disable())
				// 인가 규칙 설정
				.authorizeHttpRequests(auth -> auth
						// 인증을 받지 않고 접근할 수 있는 경로 설정
						.requestMatchers("/",
								"/error",
								"/css/**",
								"/join",
								"/login").permitAll()
						// 작성, 수정, 삭제는 로그인이 필요
						.requestMatchers("/boards/write",
								"/boards/*/edit",
								"/boards/*/delete").authenticated()
						// 목록, 상세 페이지 조회는 누구나
						.requestMatchers(HttpMethod.GET,
								"/boards",
								"/boards/*",
								"/boards/*/download").permitAll()
				)
				// 폼 로그인: 우리가 작성한 /login 페이지 사용
				.formLogin(form -> form
						// 로그인 페이지 경로
						// /user/login /member/login 등 로그인 페이지 명칭이 다르기에
						.loginPage("/login")
						// 로그인 처리 경로 -> POST 방식의 요청을 Security가 대신 인증 처리
						.loginProcessingUrl("/login")
						// 로그인 요청 시 아이디 파라미터의 이름, 기본값은 username
						.usernameParameter("loginId")
						.passwordParameter("password")
						// 로그인 성공 후 이동할 경로
						.defaultSuccessUrl("/boards")
						// 로그인 실패 시 이동할 경로
						.failureUrl("/login?error=true")
						.permitAll()).logout(logout -> logout
						//로그아웃 경로
						.logoutUrl("/logout")
						// 로그아웃 성공 시 이동할 경로
						.logoutSuccessUrl("/login")
						//로그아웃 후 세션을 초기화
						.invalidateHttpSession(true)
						//쿠키 삭제
						.deleteCookies("JSESSIONID"));
		return http.build();
	}
	
	//패스워드 암호화 (회원가입 시, 로그인 시 패스워드 비교에 사용)
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
