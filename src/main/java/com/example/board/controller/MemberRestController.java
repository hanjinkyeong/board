package com.example.board.controller;

import com.example.board.domain.Member;
import com.example.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
@RestController //API 만들 떄 사용
public class MemberRestController {
	private final MemberService memberService;
	
	//id에 해당하는 회원 정보를 조회
	@GetMapping("/{id}")
	public ResponseEntity<Member> getMember(@PathVariable Long id){
		Member member = memberService.findByMemberId(id);
		log.info("member: {}", member);
		return ResponseEntity.ok(member);
	}
	
	
}
