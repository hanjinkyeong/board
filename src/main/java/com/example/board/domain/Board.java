//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.board.domain;

import java.time.LocalDateTime;
import lombok.Generated;

public class Board {
	private Long id;
	private String title;
	private String content;
	private String writer;
	private int viewCount;
	private LocalDateTime createdAt;
	
	@Generated
	public Long getId() {
		return this.id;
	}
	
	@Generated
	public String getTitle() {
		return this.title;
	}
	
	@Generated
	public String getContent() {
		return this.content;
	}
	
	@Generated
	public String getWriter() {
		return this.writer;
	}
	
	@Generated
	public int getViewCount() {
		return this.viewCount;
	}
	
	@Generated
	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}
	
	@Generated
	public void setId(final Long id) {
		this.id = id;
	}
	
	@Generated
	public void setTitle(final String title) {
		this.title = title;
	}
	
	@Generated
	public void setContent(final String content) {
		this.content = content;
	}
	
	@Generated
	public void setWriter(final String writer) {
		this.writer = writer;
	}
	
	@Generated
	public void setViewCount(final int viewCount) {
		this.viewCount = viewCount;
	}
	
	@Generated
	public void setCreatedAt(final LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
