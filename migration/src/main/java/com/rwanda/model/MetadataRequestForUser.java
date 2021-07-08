package com.rwanda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MetadataRequestForUser {
	
	@JsonIgnore
	private boolean _reserved;
	
	public boolean is_reserved() {
		return _reserved;
	}

	public void set_reserved(boolean _reserved) {
		this._reserved = _reserved;
	}
}
