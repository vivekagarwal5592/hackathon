package com.start.Hackathon.model.assets;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssetDetails {
	
	@JsonProperty
	properties properties;
	String assetUid;
	String parentAssetUid;
	String[] eventTypes;
	String mediaType;
	String assetType;
	String coordinates;
	
	
	public properties getProperties() {
		return properties;
	}
	public void setProperties(properties properties) {
		this.properties = properties;
	}
	
	public String getAssetUid() {
		return assetUid;
	}
	public void setAssetUid(String assetUid) {
		this.assetUid = assetUid;
	}
	public String getParentAssetUid() {
		return parentAssetUid;
	}
	public void setParentAssetUid(String parentAssetUid) {
		this.parentAssetUid = parentAssetUid;
	}
	public String[] getEventTypes() {
		return eventTypes;
	}
	public void setEventTypes(String[] eventTypes) {
		this.eventTypes = eventTypes;
	}
	public String getMediaType() {
		return mediaType;
	}
	public void setMediaType(String mediaType) {
		this.mediaType = mediaType;
	}
	public String getAssetType() {
		return assetType;
	}
	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}
	public String getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
	
	
	
	

}
