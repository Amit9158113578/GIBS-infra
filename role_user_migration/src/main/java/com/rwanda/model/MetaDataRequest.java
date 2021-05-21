package com.rwanda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MetaDataRequest {

	@JsonIgnore
	private boolean _reserved;
	private int version;
	
	public boolean is_reserved() {
		return _reserved;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void set_reserved(boolean _reserved) {
		this._reserved = _reserved;
	}




}
