package com.example.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//회원가입 입력 폼
@Getter
@Setter
@NoArgsConstructor
public class MemberJoinForm {
	private String loginId;
	private String password;
	private String name;
}
