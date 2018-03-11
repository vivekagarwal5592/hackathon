package com.start.Hackathon.model.assets;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class contentdetails {
	
	@JsonProperty
	asset[] content;

	public asset[] getContent() {
		return content;
	}

	public void setContent(asset[] content) {
		this.content = content;
	}
	

}
