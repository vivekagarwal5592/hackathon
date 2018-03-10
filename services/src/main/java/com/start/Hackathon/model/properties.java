package com.start.Hackathon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class properties {
	
	String HOMOGRAPHY;
	String CENTER_GEO_COORDINATE;
	String REF_CAMERA;
	String IMAGE_SIZE;
	String VIEW;
	
	public String getHOMOGRAPHY() {
		return HOMOGRAPHY;
	}
	public void setHOMOGRAPHY(String HOMOGRAPHY) {
		this.HOMOGRAPHY = HOMOGRAPHY;
	}
	public String getCENTER_GEO_COORDINATE() {
		return CENTER_GEO_COORDINATE;
	}
	public void setCENTER_GEO_COORDINATE(String CENTER_GEO_COORDINATE) {
		this.CENTER_GEO_COORDINATE = CENTER_GEO_COORDINATE;
	}
	public String getREF_CAMERA() {
		return REF_CAMERA;
	}
	public void setREF_CAMERA(String REF_CAMERA) {
		this.REF_CAMERA = REF_CAMERA;
	}
	public String getIMAGE_SIZE() {
		return IMAGE_SIZE;
	}
	public void setIMAGE_SIZE(String IMAGE_SIZE) {
		this.IMAGE_SIZE = IMAGE_SIZE;
	}
	public String getVIEW() {
		return VIEW;
	}
	public void setVIEW(String VIEW) {
		this.VIEW = VIEW;
	}
	
	
	

}
