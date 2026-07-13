package com.example.board.service;

import com.example.board.domain.Member;
import com.example.board.repository.MemberMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor //생성자 주입
public class MemberService {
	private final MemberMemoryRepository memberRepository;
	
	//아이디 중복 확인
	public boolean isDuplicateLoginId(String loginId) {
		return memberRepository.findByLoginId(loginId).isPresent();
	}
	
	//회원 가입
	public void join(Member member){
		memberRepository.save(member);
	}
	
	// 로그인
	//Member 객체의 password 필드와 사용자가 입력한 password 값이 같으면 Member를 리턴
	//아니면 빈 Optional을 리턴
	public Optional<Member> login(String loginId, String password) {
		Optional<Member> findMember = memberRepository.findByLoginId(loginId);
		if (findMember.isPresent()) {
			//회원정보가 있으면
			if (findMember.get().getPassword().equals(password)){
				return findMember;
			}
		}
		return Optional.empty();
	}
	

}
