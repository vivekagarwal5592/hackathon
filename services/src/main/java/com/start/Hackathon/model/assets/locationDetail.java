package com.start.Hackathon.model.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class locationDetail {
	
	@JsonProperty
	location[] content;

	public location[] getContent() {
		return content;
	}

	public void setContent(location[] content) {
		this.content = content;
	}
	

}
