package com.start.Hackathon.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class parkingevent {
	
	@JsonProperty
	eventproperties content[];

	public eventproperties[] getContent() {
		return content;
	}

	public void setContent(eventproperties[] content) {
		this.content = content;
	}
	
	

}
