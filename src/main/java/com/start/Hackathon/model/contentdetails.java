package com.start.Hackathon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class contentdetails {
	
	@JsonProperty
	content[] content;

	public content[] getContent() {
		return content;
	}

	public void setContent(content[] content) {
		this.content = content;
	}

	
	
	

}
