package com.start.Hackathon.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class pedestraineventproperties {

	String locationUid;
	String assetUid;
	String eventType;
	String timestamp;

	@JsonProperty
	pedestrainproperties properties;

	@JsonProperty
	pedestrainmeasures measures;

	public String getLocationUid() {
		return locationUid;
	}

	public void setLocationUid(String locationUid) {
		this.locationUid = locationUid;
	}

	public String getAssetUid() {
		return assetUid;
	}

	public void setAssetUid(String assetUid) {
		this.assetUid = assetUid;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public pedestrainproperties getProperties() {
		return properties;
	}

	public void setProperties(pedestrainproperties properties) {
		this.properties = properties;
	}

	public pedestrainmeasures getMeasures() {
		return measures;
	}

	public void setMeasures(pedestrainmeasures measures) {
		this.measures = measures;
	}

	

}
