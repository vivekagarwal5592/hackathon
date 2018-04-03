package com.start.Hackathon.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class parkingevent {

	@JsonProperty
	parkingeventproperties content[];

	public parkingeventproperties[] getContent() {
		return content;
	}

	public void setContent(parkingeventproperties[] content) {
		this.content = content;
	}

}
