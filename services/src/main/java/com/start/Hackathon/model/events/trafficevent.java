package com.start.Hackathon.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class trafficevent {
	
	@JsonProperty
	trafficeventproperties content[];


	public trafficeventproperties[] getContent() {
		return content;
	}

	public void setContent(trafficeventproperties[] content) {
		this.content = content;
	}

	



	
	
	

}
