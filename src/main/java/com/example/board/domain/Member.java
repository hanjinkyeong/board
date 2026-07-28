//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.management.relation.RelationNotification;

@Getter @Setter
@ToString
@NoArgsConstructor
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true, nullable = false)
	private String loginId; //로그인 아이디
	private String password; //비밀번호
	private String name; //회원 이름
	
	@Enumerated(EnumType.STRING)
	private  Role role;
	
	public Member(String loginId, String password, String name, Role role) {
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.role = role;
	}
	
	
	
}
