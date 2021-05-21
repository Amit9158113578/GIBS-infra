package com.rwanda.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RequestForRoles {
	
	@JsonIgnore
	private String name;
	private ElasticSearch elasticsearch;
	private List<Kibana> kibana;
	private MetaDataRequest metadata;
	public ElasticSearch getElasticsearch() {
		return elasticsearch;
	}
	public void setElasticsearch(ElasticSearch elasticsearch) {
		this.elasticsearch = elasticsearch;
	}
	public List<Kibana> getKibana() {
		return kibana;
	}
	public void setKibana(List<Kibana> kibana) {
		this.kibana = kibana;
	}
	public MetaDataRequest getMetadata() {
		return metadata;
	}
	public void setMetadata(MetaDataRequest metadata) {
		this.metadata = metadata;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
