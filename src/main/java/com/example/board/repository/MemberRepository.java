package com.example.board.repository;


import com.example.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	
	// loginId로 Member 정보를 조회
	// (쿼리 메서드 -> 이름. 규칙만으로 Spring Data JPA가 구현체를 만들어준다.)
	// select * from member where login _id = ?
	Optional<Member> findByLoginId(String loginId);
	
}
