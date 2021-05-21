package com.rwanda.model;

import java.util.List;

public class Index {

	private List<String> names;
	private List<String> privileges;
	private String allow_restricted_indices;
	public List<String> getNames() {
		return names;
	}
	public void setNames(List<String> names) {
		this.names = names;
	}
	public List<String> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
	public String getAllow_restricted_indices() {
		return allow_restricted_indices;
	}
	public void setAllow_restricted_indices(String allow_restricted_indices) {
		this.allow_restricted_indices = allow_restricted_indices;
	}
	
	
}
