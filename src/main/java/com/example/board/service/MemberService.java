package com.example.board.service;

import com.example.board.domain.Member;
import com.example.board.domain.Role;
import com.example.board.dto.MemberJoinForm;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor       // 생성자 주입
@Transactional(readOnly = true) //리드 온리 안붙이면 모든 메서드에서 트랜젝션 작업을 하기 때문(데이터 변경 X 할필요없다)
public class MemberService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	// 아이디 중복 확인 메서드
	public boolean isDuplicateLoginId(String loginId){
//      return memberRepository.findById()
		return memberRepository.findByLoginId(loginId).isPresent();
	}
	
	
	// 회원가입
	@Transactional
	public void join(MemberJoinForm joinForm){
		Member member = new Member(
				joinForm.getLoginId(),
				passwordEncoder.encode(joinForm.getPassword()),
				joinForm.getName(),
				Role.ROLE_USER);

      memberRepository.save(member);
	}
	
	// 로그인
	// Member 객체의 password 필드와 사용자가 입력한 password 값이 같으면 Member를 리턴
	// 아니면 빈 Optional을 리턴
	public Optional<Member> login(String loginId, String password) {     // 로그인 아이디로 회원 정보를 가져옴
//      Optional<Member> member = memberRepository.findByLoginId(loginId);
//      if (member.isPresent()) {
//         Member findmember = member.get();
//         if (findMember.getPassword().equals(password)) {   // equals 사용자가 입력한 정보 -> 같다면
//            return member;
//         }
//      }
//		return Optional.empty();
		return memberRepository.findByLoginId(loginId)
				.filter(member -> member.getPassword().equals(password));
	}
	
	//회원정보 조회
	public Member findByMemberId(Long id){
		return memberRepository.findById(id)
				.orElseThrow(() -> new RuntimeException(id + "회원정보가 존재하지 않습니다."));
	}
	
}