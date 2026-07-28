package com.example.board;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.domain.Role;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class InitData {
	
	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Bean
	public CommandLineRunner init() {
		return args -> {
			
			Member member = memberRepository.findByLoginId("1111").orElse(null);
			
			if (member == null) {
				member = new Member(
						"1111",
						passwordEncoder.encode("1111"),
						"사용자1",
						Role.ROLE_ADMIN
				);
				member = memberRepository.save(member);
			} else {
				member.setPassword(passwordEncoder.encode("1111"));
				member.setRole(Role.ROLE_ADMIN);
				memberRepository.save(member);
			}
			
			Member member2 = memberRepository.findByLoginId("2222").orElse(null);
			
			if (member2 == null) {
				member2 = new Member(
						"2222",
						passwordEncoder.encode("2222"),
						"사용자2",
						Role.ROLE_ADMIN
				);
				member2 = memberRepository.save(member2);
			} else {
				member2.setPassword(passwordEncoder.encode("2222"));
				member2.setRole(Role.ROLE_ADMIN);
				memberRepository.save(member2);
			}
			
			if (boardRepository.count() == 0) {
				boardRepository.save(new Board("첫 번째 글", "내용1", member));
				boardRepository.save(new Board("두 번째 글", "내용2", member2));
				boardRepository.save(new Board("세 번째 글", "내용3", member2));
			}
		};
	}
}