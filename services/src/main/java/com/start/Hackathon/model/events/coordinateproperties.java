package com.start.Hackathon.model.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class coordinateproperties {
	
	String orgPixelCoordinates;
	String pixelCoordinates;
	String objectUid;
	String geoCoordinates;
	String imageAssetUid;
	
	public String getOrgPixelCoordinates() {
		return orgPixelCoordinates;
	}
	public void setOrgPixelCoordinates(String orgPixelCoordinates) {
		this.orgPixelCoordinates = orgPixelCoordinates;
	}
	public String getPixelCoordinates() {
		return pixelCoordinates;
	}
	public void setPixelCoordinates(String pixelCoordinates) {
		this.pixelCoordinates = pixelCoordinates;
	}
	public String getObjectUid() {
		return objectUid;
	}
	public void setObjectUid(String objectUid) {
		this.objectUid = objectUid;
	}
	public String getGeoCoordinates() {
		return geoCoordinates;
	}
	public void setGeoCoordinates(String geoCoordinates) {
		this.geoCoordinates = geoCoordinates;
	}
	public String getImageAssetUid() {
		return imageAssetUid;
	}
	public void setImageAssetUid(String imageAssetUid) {
		this.imageAssetUid = imageAssetUid;
	}
	
	

}
