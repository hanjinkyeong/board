package com.example.board.repository;
import com.example.board.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemberMemoryRepository {
	private final Map<String, Member> store =
			new HashMap<>();
	
	public Member save(Member member) {
		store.put(member.getLoginId(),member);
		return member;
	}
	public Optional<Member> findByLoginId(String loginId) {
		return Optional.ofNullable(store.get(loginId));
	}
	
	
	}

