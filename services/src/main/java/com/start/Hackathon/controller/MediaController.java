package com.start.Hackathon.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.start.Hackathon.model.assets.contentdetails;
import com.start.Hackathon.model.media.entries;
import com.start.Hackathon.model.media.media;
import com.start.Hackathon.model.media.mediastep1;
import com.start.Hackathon.model.media.mediastep2;

@RestController
public class MediaController {

	@Autowired
	private RestOperations restoperations;

	private final String url;
	private final String authorization;
	private static int imageid;
	RestTemplate restTemplate;

	@Autowired
	public MediaController(@Value("${predix.asset.media.restHost}") final String url,
			@Value("${predix.bearer.token}") final String authorization) {
		this.url = url;
		this.authorization = authorization;
		restTemplate = new RestTemplate();
	}

	@RequestMapping(value = "/getmediabyasset", method = RequestMethod.POST)
	public List<byte[]> getmediabyasset(@RequestParam String assetuid, @RequestParam String startts,
			@Value("${predix.asset.image.zoneid}") final String predixZoneId) throws IOException {

		ResponseEntity<mediastep1> m1 = restTemplate.exchange(
				url + "ondemand/assets/" + assetuid + "/media?mediaType=IMAGE&timestamp=" + startts, HttpMethod.GET,
				getHeaders(), mediastep1.class);

		String url1 = m1.getBody().getPollUrl();
		// System.out.println(m1.getBody().getPollUrl());
		ResponseEntity<mediastep2> m2 = restTemplate.exchange(url1, HttpMethod.GET, getHeaders(), mediastep2.class);

		media[] m = m2.getBody().getListOfEntries().getContent();

		List<byte[]> is = new ArrayList<byte[]>();

		for (media x : m) {
			ResponseEntity<byte[]> i = restTemplate.exchange(x.getUrl(), HttpMethod.GET, getHeaders(), byte[].class);
			System.out.println("before printing image");
			byte[] xe = i.getBody();
			is.add(xe);
			Files.write(Paths.get("image" + String.valueOf(imageid++) + ".jpg"), xe);
			// System.out.println(i);
		}

		return is;

	}

	@RequestMapping(value = "/getvideobyasset", method = RequestMethod.POST)
	public List<byte[]> getvideobyasset(@RequestParam String assetuid, @RequestParam String startts,
			@Value("${predix.asset.video.zoneid}") final String predixZoneId) {

		System.out.println("assetuid");
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();

		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", predixZoneId);

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		System.out.println(url + "ondemand/assets/" + assetuid + "/media?mediaType=VIDEO&timestamp=" + startts);
		ResponseEntity<mediastep1> m1 = restTemplate.exchange(
				url + "ondemand/assets/" + assetuid + "/media?mediaType=VIDEO&timestamp=" + startts, HttpMethod.GET,
				entity, mediastep1.class);

		String url1 = m1.getBody().getPollUrl();
		System.out.println(m1.getBody().getPollUrl());
		ResponseEntity<mediastep2> m2 = restTemplate.exchange(url1, HttpMethod.GET, entity, mediastep2.class);

		media[] m = m2.getBody().getListOfEntries().getContent();

		List<byte[]> is = new ArrayList<byte[]>();

		for (media x : m) {
			ResponseEntity<byte[]> i = restTemplate.exchange(x.getUrl(), HttpMethod.GET, entity, byte[].class);
			System.out.println("before printing image");
			// Files.write(Paths.get("image.jpg"), i);
			byte[] xe = i.getBody();
			is.add(xe);
			try {
				Files.write(Paths.get("video.mp4"), xe);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(i);
		}

		return is;

	}

	@RequestMapping(value = "/getmediabylocation", method = RequestMethod.POST)
	public List<byte[]> getmediabylocation(@RequestParam String location, @RequestParam String startts,
			@Value("${predix.asset.image.zoneid}") final String predixZoneId) throws IOException {

	
		ResponseEntity<mediastep1> m1 = restTemplate.exchange(
				url + "ondemand/locations/" + location + "/media?mediaType=IMAGE&timestamp=" + startts, HttpMethod.GET,
				getHeaders(), mediastep1.class);

		String url1 = m1.getBody().getPollUrl();
		System.out.println(m1.getBody().getPollUrl());
		ResponseEntity<mediastep2> m2 = restTemplate.exchange(url1, HttpMethod.GET, getHeaders(), mediastep2.class);

		media[] m = m2.getBody().getListOfEntries().getContent();

		List<byte[]> is = new ArrayList<byte[]>();

		for (media x : m) {
			ResponseEntity<byte[]> i = restTemplate.exchange(x.getUrl(), HttpMethod.GET, getHeaders(), byte[].class);
			System.out.println("before printing image");
			byte[] xe = i.getBody();
			is.add(xe);
			Files.write(Paths.get("image.jpg"), xe);
			// System.out.println(i);
		}

		return is;

	}

	@RequestMapping(value = "/getvideobylocation", method = RequestMethod.POST)
	public List<byte[]> getvideobylocation(@RequestParam String location, @RequestParam String startts,
			@Value("${predix.asset.video.zoneid}") final String predixZoneId) throws IOException {

		ResponseEntity<mediastep1> m1 = restTemplate.exchange(
				url + "ondemand/locations/" + location + "/media?mediaType=VIDEO&timestamp=" + startts, HttpMethod.GET,
				getHeaders(), mediastep1.class);

		String url1 = m1.getBody().getPollUrl();
		System.out.println(m1.getBody().getPollUrl());
		ResponseEntity<mediastep2> m2 = restTemplate.exchange(url1, HttpMethod.GET, getHeaders(), mediastep2.class);

		media[] m = m2.getBody().getListOfEntries().getContent();

		List<byte[]> is = new ArrayList<byte[]>();

		for (media x : m) {
			ResponseEntity<byte[]> i = restTemplate.exchange(x.getUrl(), HttpMethod.GET, getHeaders(), byte[].class);
			System.out.println("before printing image");
			byte[] xe = i.getBody();
			is.add(xe);
			Files.write(Paths.get("video.mp4"), xe);
			// System.out.println(i);
		}

		return is;

	}

	public HttpEntity<String> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PARKING");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

}
