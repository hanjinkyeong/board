//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.board.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
@NoArgsConstructor
@Getter @Setter
@Entity
public class Board {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //식별자
	private String title; //제목
	
	@Column(columnDefinition = "TEXT")
	private String content; //내용
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member-id")
	private Member member; //작성자(회원아이디)
	private int viewCount; //조회수
	private LocalDateTime createdAt; //작성 시간
	
	// 첨부파일
	private String originalFileName; //사용자가 올린 원래 파일명
	private String storedFileName; //서버에 저장된 실제 파일명
	
	public Board(String title, String content, Member member) {
		this.title = title;
		this.content = content;
		this.member = member;
		this.viewCount = 0;
		this.createdAt = LocalDateTime.now();
	}
	
	// 첨부파일이 있는지 확인
	public boolean isHasAttachment(){
		return storedFileName != null;
	}
}
