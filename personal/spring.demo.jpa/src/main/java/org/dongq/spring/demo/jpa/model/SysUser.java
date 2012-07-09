package org.dongq.spring.demo.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "SYS_USER")
public class SysUser extends AbstractPersistable<Long>/**/{

	private static final long serialVersionUID = 1L;

	@Column(name = "user_name")
	private String username;

	@Column(name = "real_name")
	private String realname;

	private String mobile;

	private String email;

	@Override
	@GeneratedValue(generator = "sequence")
	@GenericGenerator(name = "sequence", strategy = "sequence", parameters = { @Parameter(name = "sequence", value = "SEQ_SYS_USER") })
	public Long getId() {
		return super.getId();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "SysUser [username=" + username + ", realname=" + realname + ", mobile=" + mobile + ", email=" + email + ", getId()="
				+ getId() + ", isNew()=" + isNew() + "]";
	}


}
