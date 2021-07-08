package com.rwanda.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(Include.NON_NULL)
public class Feature {

	List<String> discover;
	List<String> dashboard;
	List<String> canvas;
	List<String> ml;
	List<String> visualize;
	List<String> maps;
	List<String> indexPatterns;
	List<String> savedObjectsTagging;
	List<String> logs;
	List<String> enterpriseSearch;
	List<String> infrastructure;
	List<String> apm;
	List<String> uptime;
	List<String> siem;
	List<String> dev_tools;
	List<String> advancedSettings;
	List<String> savedObjectsManagement;
	List<String> fleet;
	List<String> actions;
	List<String> stackAlerts;
	List<String> monitoring;
	
	
	
	public List<String> getIndexPatterns() {
		return indexPatterns;
	}
	public void setIndexPatterns(List<String> indexPatterns) {
		this.indexPatterns = indexPatterns;
	}
	public List<String> getSavedObjectsTagging() {
		return savedObjectsTagging;
	}
	public void setSavedObjectsTagging(List<String> savedObjectsTagging) {
		this.savedObjectsTagging = savedObjectsTagging;
	}
	public List<String> getLogs() {
		return logs;
	}
	public void setLogs(List<String> logs) {
		this.logs = logs;
	}
	public List<String> getEnterpriseSearch() {
		return enterpriseSearch;
	}
	public void setEnterpriseSearch(List<String> enterpriseSearch) {
		this.enterpriseSearch = enterpriseSearch;
	}
	public List<String> getInfrastructure() {
		return infrastructure;
	}
	public void setInfrastructure(List<String> infrastructure) {
		this.infrastructure = infrastructure;
	}
	public List<String> getApm() {
		return apm;
	}
	public void setApm(List<String> apm) {
		this.apm = apm;
	}
	public List<String> getUptime() {
		return uptime;
	}
	public void setUptime(List<String> uptime) {
		this.uptime = uptime;
	}
	public List<String> getSiem() {
		return siem;
	}
	public void setSiem(List<String> siem) {
		this.siem = siem;
	}
	public List<String> getDev_tools() {
		return dev_tools;
	}
	public void setDev_tools(List<String> dev_tools) {
		this.dev_tools = dev_tools;
	}
	public List<String> getAdvancedSettings() {
		return advancedSettings;
	}
	public void setAdvancedSettings(List<String> advancedSettings) {
		this.advancedSettings = advancedSettings;
	}
	public List<String> getSavedObjectsManagement() {
		return savedObjectsManagement;
	}
	public void setSavedObjectsManagement(List<String> savedObjectsManagement) {
		this.savedObjectsManagement = savedObjectsManagement;
	}
	public List<String> getFleet() {
		return fleet;
	}
	public void setFleet(List<String> fleet) {
		this.fleet = fleet;
	}
	public List<String> getActions() {
		return actions;
	}
	public void setActions(List<String> actions) {
		this.actions = actions;
	}
	public List<String> getStackAlerts() {
		return stackAlerts;
	}
	public void setStackAlerts(List<String> stackAlerts) {
		this.stackAlerts = stackAlerts;
	}
	public List<String> getMonitoring() {
		return monitoring;
	}
	public void setMonitoring(List<String> monitoring) {
		this.monitoring = monitoring;
	}
	public List<String> getDiscover() {
		return discover;
	}
	public void setDiscover(List<String> discover) {
		this.discover = discover;
	}
	public List<String> getDashboard() {
		return dashboard;
	}
	public void setDashboard(List<String> dashboard) {
		this.dashboard = dashboard;
	}
	public List<String> getCanvas() {
		return canvas;
	}
	public void setCanvas(List<String> canvas) {
		this.canvas = canvas;
	}
	public List<String> getMl() {
		return ml;
	}
	public void setMl(List<String> ml) {
		this.ml = ml;
	}
	public List<String> getVisualize() {
		return visualize;
	}
	public void setVisualize(List<String> visualize) {
		this.visualize = visualize;
	}
	public List<String> getMaps() {
		return maps;
	}
	public void setMaps(List<String> maps) {
		this.maps = maps;
	}
	
	
};
