//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.board.domain;

import lombok.Generated;

public class Member {
	private String loginId;
	private String password;
	private String name;
	
	@Generated
	public String getLoginId() {
		return this.loginId;
	}
	
	@Generated
	public String getPassword() {
		return this.password;
	}
	
	@Generated
	public String getName() {
		return this.name;
	}
	
	@Generated
	public void setLoginId(final String loginId) {
		this.loginId = loginId;
	}
	
	@Generated
	public void setPassword(final String password) {
		this.password = password;
	}
	
	@Generated
	public void setName(final String name) {
		this.name = name;
	}
	
	@Generated
	public String toString() {
		String var10000 = this.getLoginId();
		return "Member(loginId=" + var10000 + ", password=" + this.getPassword() + ", name=" + this.getName() + ")";
	}
}
