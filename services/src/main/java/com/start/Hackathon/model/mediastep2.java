package com.start.Hackathon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class mediastep2 {
	
	String status;
	
	@JsonProperty
	entries listOfEntries;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public entries getListOfEntries() {
		return listOfEntries;
	}
	public void setListOfEntries(entries listOfEntries) {
		this.listOfEntries = listOfEntries;
	}
	
	

}
