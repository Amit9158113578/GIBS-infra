package com.rwanda.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Kibana {
	
	private List<String> base;
	private Feature feature;
	private List<String> spaces;
	public List<String> getBase() {
		return base;
	}
	public void setBase(List<String> base) {
		this.base = base;
	}
	public Feature getFeature() {
		return feature;
	}
	public void setFeature(Feature feature) {
		this.feature = feature;
	}
	public List<String> getSpaces() {
		return spaces;
	}
	public void setSpaces(List<String> spaces) {
		this.spaces = spaces;
	}
	
	
	
}
