package com.start.Hackathon.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class pedestrainevent {
	
	@JsonProperty
	pedestraineventproperties content[];

	public pedestraineventproperties[] getContent() {
		return content;
	}

	public void setContent(pedestraineventproperties[] content) {
		this.content = content;
	}


	

	



	
	
	

}
