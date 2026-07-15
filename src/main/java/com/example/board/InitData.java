package com.example.board;

import com.example.board.domain.Member;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InitData {
	private final MemberRepository memberRepository;
	
	@Bean
	public CommandLineRunner init() {
		return args -> {
			
			if(memberRepository.findByLoginId("user1").isEmpty()) {
				
				Member member = new Member(
						"1111",
						"1111",
						"사용자1"
				);
				
				memberRepository.save(member);
			}
		};
	}
}
