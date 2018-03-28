package com.start.Hackathon.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class parkingeventproperties {
	
	String locationUid;
	String assetUid;
	String eventType;
	String timestamp;
	
	@JsonProperty
	coordinateproperties properties;

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

	public coordinateproperties getProperties() {
		return properties;
	}

	public void setProperties(coordinateproperties properties) {
		this.properties = properties;
	}
	
	

	
	/*String speedUnit;
	String eventUid;
	String directionUnit;
	String counter_direction_vehicleType;
	String vehicletype;
	
	String counter_direction;
	String counter_direction_speed;
	String counter_direction_vehicleCount;
	String direction;
	Float speed;
	Integer vehicleCount;*/
	
	
	

}
