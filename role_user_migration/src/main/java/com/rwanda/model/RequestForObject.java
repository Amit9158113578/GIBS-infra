package com.rwanda.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RequestForObject {

	String [] type;
	String includeReferencesDeep;
	
	public String[] getType() {
		return type;
	}

	public void setType(String[] type) {
		this.type = type;
	}

	public String getIncludeReferencesDeep() {
		return includeReferencesDeep;
	}

	public void setIncludeReferencesDeep(String includeReferencesDeep) {
		this.includeReferencesDeep = includeReferencesDeep;
	}

	
}
