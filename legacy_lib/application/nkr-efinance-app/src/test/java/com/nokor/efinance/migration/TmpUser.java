package com.nokor.efinance.migration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.seuksa.frmk.model.entity.EntityA;

/**
 * 
 * @author uhout.cheng
 *
 */
@Entity
@Table(name="tu_sec_user_tmp")
public class TmpUser extends EntityA {
	/** */
	private static final long serialVersionUID = -4812480793472869917L;

	private String login;
	private String pwd;
	private String profileCode;
	private String lastName;
	private String firstName;
	
	@Override
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	 
	/**
	 * @return the login
	 */
    @Column(name = "login", nullable = false)
	public String getLogin() {
		return login;
	}
	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	/**
	 * @return the pwd
	 */
    @Column(name = "pwd", nullable = false)
	public String getPwd() {
		return pwd;
	}
	/**
	 * @param pwd the pwd to set
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	/**
	 * @return the profileCode
	 */
    @Column(name = "role_code", nullable = false)
	public String getProfileCode() {
		return profileCode;
	}
	/**
	 * @param profileCode the profileCode to set
	 */
	public void setProfileCode(String profileCode) {
		this.profileCode = profileCode;
	}
	/**
	 * @return the lastName
	 */
    @Column(name = "lastname", nullable = false)
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the firstName
	 */
    @Column(name = "firstname", nullable = false)
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	

}
