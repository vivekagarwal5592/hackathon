package com.start.Hackathon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.start.Hackathon.model.contentdetails;
import com.start.Hackathon.model.mediastep1;

public class MediaController {
	
	
	@Autowired
	private RestOperations restoperations;

	private final String url;
	private final String authorization;

	@Autowired
	public MediaController(@Value("${predix.asset.mediadata.restHost}") final String url,
			@Value("${predix.bearer.token}") final String authorization) {
		this.url = url;
		this.authorization = authorization;
	}

	//dummy method
	@RequestMapping(value = "/getMedia", method = RequestMethod.GET)
	public void getallassets(@RequestParam String assetuid,@RequestParam String startts) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization",
				"Bearer "+authorization);
		headers.set("Predix-Zone-Id", "SD-IE-TRAFFIC");

		System.out.println(" Predix zone id is ${predix.asset.traffic.zoneid}");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		ResponseEntity<mediastep1> m1 =  restTemplate.exchange(url
				+ "/ondemand/assets/"+assetuid+"/media?mediaType=IMAGE&timestamp="+startts,
				HttpMethod.GET, entity, mediastep1.class);
		
		
		
		

		// return restoperations.getForObject(url,companydetails.class);
	}
	
	@RequestMapping(value = "/getallAssetswithinbbox", method = RequestMethod.POST)
	public ResponseEntity<contentdetails> getallassetswithinarea(@RequestParam String lattitude,@RequestParam String longitude) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization",
				"Bearer "+authorization);
		headers.set("Predix-Zone-Id", "SD-IE-TRAFFIC");

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		return restTemplate.exchange(url
				+ "assets/search?bbox="+lattitude+","+longitude+"&page=0&size=200&q=assetType:CAMERA",
				HttpMethod.GET, entity, contentdetails.class);

		// return restoperations.getForObject(url,companydetails.class);
	}
	
	
	
	
	

}
