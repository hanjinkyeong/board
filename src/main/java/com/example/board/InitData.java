package com.example.board;

import com.example.board.domain.Board;
import com.example.board.domain.Member;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InitData {
	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	@Bean
	public CommandLineRunner init() {
		return args -> {
			
			if (memberRepository.count() == 0) {
				
				Member member = new Member("1111","1111","사용자1");
				Member member2 = new Member("2222","2222","사용자2");
				
				member = memberRepository.save(member);
				member2 = memberRepository.save(member2);
				
				boardRepository.save(new Board("첫 번째 글","내용1",member));
				boardRepository.save(new Board("두 번째 글","내용2",member2));
				boardRepository.save(new Board("세 번째 글","내용3",member2));
			}
		};
	}
}
