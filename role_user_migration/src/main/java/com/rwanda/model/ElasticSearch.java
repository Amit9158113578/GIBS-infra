package com.rwanda.model;

import java.util.List;

public class ElasticSearch {

	private List<String> cluster;
	private List<Index> indices;
	private List<String> run_as;
	public List<String> getCluster() {
		return cluster;
	}
	public void setCluster(List<String> cluster) {
		this.cluster = cluster;
	}
	public List<Index> getIndices() {
		return indices;
	}
	public void setIndices(List<Index> indices) {
		this.indices = indices;
	}
	public List<String> getRun_as() {
		return run_as;
	}
	public void setRun_as(List<String> run_as) {
		this.run_as = run_as;
	}
	
	
	
}
