package com.start.Hackathon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class entries {
	
	@JsonProperty
	media[] content;

	public media[] getContent() {
		return content;
	}

	public void setContent(media[] content) {
		this.content = content;
	}
	
	

}
