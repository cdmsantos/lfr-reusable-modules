package com.liferay.birthday.user.dto;

import java.io.Serializable;

/**
 * @author crystalsantos
 */
public class UserDTO implements Serializable {

	private static final long serialVersionUID = -343108936025496771L;
		
	String birthDay;	
	String birthMonth;
	String job;
	String name;

	public UserDTO(String birthDay, String birthMonth, String job, String name) {
		this.birthDay = birthDay;
		this.birthMonth = birthMonth;
		this.job = job;
		this.name = name;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	
	public String getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonths(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
