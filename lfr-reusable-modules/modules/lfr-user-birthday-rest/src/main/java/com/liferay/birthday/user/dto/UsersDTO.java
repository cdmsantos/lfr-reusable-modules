package com.liferay.birthday.user.dto;

import java.io.Serializable;
import java.util.List;

public class UsersDTO implements Serializable {

	private static final long serialVersionUID = -8656634778356864519L;
	
	List<UserDTO> users;
	
	public UsersDTO(List<UserDTO> users) {
		this.users = users;
	}

	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<UserDTO> users) {
		this.users = users;
	}
	
}
