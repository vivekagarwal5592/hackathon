package com.start.Hackathon.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.start.Hackathon.model.AssetDetails;
import com.start.Hackathon.model.contentdetails;

@RestController
public class AssetController {

	@Autowired
	private RestOperations restoperations;

	private final String url;
	private final String authorization;

	@Autowired
	public AssetController(@Value("${predix.asset.metadata.restHost}") final String url,
			@Value("${predix.bearer.token}") final String authorization) {
		this.url = url;
		this.authorization = authorization;
	}

	//dummy method
	@RequestMapping(value = "/getallAssets", method = RequestMethod.GET)
	public ResponseEntity<contentdetails> getallassets() {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization",
				"Bearer "+authorization);
		headers.set("Predix-Zone-Id", "SD-IE-TRAFFIC");

		System.out.println(" Predix zone id is ${predix.asset.traffic.zoneid}");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		return restTemplate.exchange(url
				+ "assets/search?bbox=33.077762:-117.663817,32.559574:-116.584410&page=0&size=200&q=assetType:CAMERA",
				HttpMethod.GET, entity, contentdetails.class);

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
	
	
	@RequestMapping(value = "/getSingleAssetDetails", method = RequestMethod.POST)
	public ResponseEntity<AssetDetails> getallSingleAssetDetails(@RequestParam String assetuid) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization",
				"Bearer "+authorization);
		headers.set("Predix-Zone-Id", "SD-IE-TRAFFIC");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		
		return restTemplate.exchange(url
				+ "assets/"+ "522de83f-e524-4f76-80f0-463d3815b7a4",
				HttpMethod.GET, entity, AssetDetails.class);

		// return restoperations.getForObject(url,companydetails.class);
	}

	/*
	 * @RequestMapping(value = "/getassetsByAssetId", method = RequestMethod.GET)
	 * public Cards getContract5() { return restoperations.getForObject(url,
	 * Cards.class); }
	 */

}
