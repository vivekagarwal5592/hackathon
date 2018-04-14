package com.start.Hackathon.controller;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.client.RestTemplate;

import com.start.Hackathon.model.assets.PedestrainDetail;
import com.start.Hackathon.model.assets.location;
import com.start.Hackathon.model.assets.locationDetail;
import com.start.Hackathon.model.customModels.locationsummary;
import com.start.Hackathon.model.customModels.parkingDetails;
import com.start.Hackathon.model.customModels.parkingsummary;
import com.start.Hackathon.model.customModels.pedestrainsummary;
import com.start.Hackathon.model.customModels.trafficsummary;
import com.start.Hackathon.model.events.parkingevent;
import com.start.Hackathon.model.events.pedestrainevent;
import com.start.Hackathon.model.events.trafficevent;

@RestController
public class MachineLearning {

	RestTemplate restTemplate;
	private final String url;
	private final String authorization;
	private static int imageid;

	@Autowired
	public MachineLearning(@Value("${predix.asset.event.restHost}") final String url,
			@Value("${predix.bearer.token}") final String authorization) {
		this.url = url;
		this.authorization = authorization;
		restTemplate = new RestTemplate();
	}

	@RequestMapping(value = "/getparkingsummarycsv", method = RequestMethod.POST)
	public ResponseEntity<List<parkingsummary>> getlocationsummary(@RequestParam String startts,
			@RequestParam String endts, @RequestParam String bbox, boolean hourly) {

		// System.out.println("I am in locationsummary");
		String url = "https://ic-metadata-service-sdhack.run.aws-usw02-pr.ice.predix.io/v2/metadata/";
		locationDetail parkingDetail = restTemplate
				.exchange(url + "locations/search?q=locationType:PARKING_ZONE&bbox=" + bbox + "&page=0&size=50",
						HttpMethod.GET, getParkingHeaders(), locationDetail.class)
				.getBody();

		getParkingZonesLoactionscsv(parkingDetail);

		// System.out.println(parkingDetail.getContent().length);

		List<parkingsummary> psummary = new ArrayList<parkingsummary>();
		for (location l : parkingDetail.getContent()) {
			parkingsummary p = getPKINForLastTendays(l.getLocationUid(), startts, endts, hourly).getBody();
			if (p.getLocationUid() != null) {
				psummary.add(p);
			}

		}

		downloadparkingsummarycsv(psummary);

		return new ResponseEntity<List<parkingsummary>>(psummary, getResponseHeaders(), HttpStatus.CREATED);

	}

	@RequestMapping(value = "/getpedestrainsummarycsv", method = RequestMethod.POST)
	public ResponseEntity<List<pedestrainsummary>> getpedestrainsummary(@RequestParam String startts,
			@RequestParam String endts, @RequestParam String bbox, boolean hourly) {

		

		String url = "https://ic-metadata-service-sdhack.run.aws-usw02-pr.ice.predix.io/v2/metadata/";

		PedestrainDetail pedestrainDetail = restTemplate
				.exchange(url + "locations/search?q=locationType:WALKWAY&bbox=" + bbox + "&page=0&size=50",
						HttpMethod.GET, getPedestrainHeaders(), PedestrainDetail.class)
				.getBody();
		
		getPedestrainZonesLoactionscsv(pedestrainDetail);

		System.out.println(pedestrainDetail.getContent().length);
		List<pedestrainsummary> pedestrainsummary = new ArrayList<pedestrainsummary>();
		for (location l : pedestrainDetail.getContent()) {
			pedestrainsummary t = getPedestrainForLastTenDays(l.getLocationUid(), startts, endts, hourly).getBody();
			if (t != null) {
				pedestrainsummary.add(t);
				(pedestrainsummary.get(pedestrainsummary.size() - 1)).setLocationcoordinates(l.getCoordinates());
			}
		}

		System.out.println("I am going to download csv");
		downloadpedestrainsummarycsv(pedestrainsummary);

		return new ResponseEntity<List<pedestrainsummary>>(pedestrainsummary, getResponseHeaders(), HttpStatus.CREATED);

	}

	@RequestMapping(value = "/gettrafficsummarycsv", method = RequestMethod.POST)
	public ResponseEntity<List<trafficsummary>> getOflisttrafficsummary(@RequestParam String startts,
			@RequestParam String endts, @RequestParam String bbox, boolean hourly) {

		String url = "https://ic-metadata-service-sdhack.run.aws-usw02-pr.ice.predix.io/v2/metadata/";

		locationDetail trafficDetail = restTemplate
				.exchange(url + "locations/search?q=locationType:TRAFFIC_LANE&bbox=" + bbox + "&page=0&size=50",
						HttpMethod.GET, getTrafficHeaders(), locationDetail.class)
				.getBody();
		
		getTrafficZonesLoactionscsv(trafficDetail );

		List<trafficsummary> tsummary = new ArrayList<trafficsummary>();
		for (location l : trafficDetail.getContent()) {
			trafficsummary t = getTrafficForLastTenDays(l.getLocationUid(), startts, endts, hourly).getBody();
			if (t != null) {
				tsummary.add(t);
				(tsummary.get(tsummary.size() - 1)).setLocationcoordinates(l.getCoordinates());
			}
		}

		System.out.println("I am going to download csv");
		downloadtrafficsummarycsv(tsummary);

		return new ResponseEntity<List<trafficsummary>>(tsummary, getResponseHeaders(), HttpStatus.CREATED);
	}
	
	
	

	public ResponseEntity<trafficsummary> getTrafficForLastTenDays(@RequestParam String traffic_loc,
			@RequestParam String startts, @RequestParam String endts, boolean hourly) {
		trafficsummary t = new trafficsummary();

		String starttime = getdateinTimeStamp(startts);
		String endtime = getdateinTimeStamp(endts);

		while ((Long.parseLong(starttime) + 21600000) <= Long.parseLong(endtime)) {

			trafficevent traffic = restTemplate.exchange(
					url + "locations/" + traffic_loc + "/events?eventType=TFEVT&" + "startTime=" + starttime
							+ "&endTime=" + String.valueOf(Long.parseLong(starttime) + 21600000),
					HttpMethod.GET, getTrafficHeaders(), trafficevent.class).getBody();
			getTrafficSummary(t, traffic, hourly);
			starttime = String.valueOf(Long.parseLong(starttime) + 21600000);
		}

		return new ResponseEntity<trafficsummary>(t, getResponseHeaders(), HttpStatus.CREATED);

	}

	public ResponseEntity<pedestrainsummary> getPedestrainForLastTenDays(@RequestParam String pedestrain_loc,
			@RequestParam String startts, @RequestParam String endts, boolean hourly) {
		pedestrainsummary t = new pedestrainsummary();

		String starttime = getdateinTimeStamp(startts);
		String endtime = getdateinTimeStamp(endts);

		

		while ((Long.parseLong(starttime) + 21600000) <= Long.parseLong(endtime)) {

			pedestrainevent pedestrain = restTemplate.exchange(
					url + "locations/" + pedestrain_loc + "/events?eventType=PEDEVT&" + "startTime=" + starttime
							+ "&endTime=" + String.valueOf(Long.parseLong(starttime) + 21600000),
					HttpMethod.GET, getPedestrainHeaders(), pedestrainevent.class).getBody();
			getPedestrainSummary(t, pedestrain, hourly);
			starttime = String.valueOf(Long.parseLong(starttime) + 21600000);
		}

		return new ResponseEntity<pedestrainsummary>(t, getResponseHeaders(), HttpStatus.CREATED);

	}

	public trafficsummary getTrafficSummary(trafficsummary t, trafficevent traffic, boolean hourly) {

		if (traffic.getContent().length > 0) {

			t.setLocationUid(traffic.getContent()[0].getLocationUid());

			for (int i = 0; i <= traffic.getContent().length - 1; i++) {
				t.setNoOfVehivles(t.getNoOfVehivles() + traffic.getContent()[i].getMeasures().getVehicleCount());

				if (traffic.getContent()[i].getTimestamp() != null) {
					Date currentDate = new Date(Long.parseLong(traffic.getContent()[i].getTimestamp()));

					String d = null;
					if (hourly) {
						d = new SimpleDateFormat("MM/dd/yyyy hh a").format(currentDate).toString();
					} else {
						d = new SimpleDateFormat("MM/dd/yyyy").format(currentDate).toString();
					}

					// System.out.println(d);
					if (t.getNumberOfCarsSpotted().containsKey(d)) {
						t.getNumberOfCarsSpotted().put(d, t.getNumberOfCarsSpotted().get(d)
								+ traffic.getContent()[i].getMeasures().getVehicleCount());
					} else {
						t.getNumberOfCarsSpotted().put(d, traffic.getContent()[i].getMeasures().getVehicleCount());
					}
				}
			}

		}

		return t;
	}

	public pedestrainsummary getPedestrainSummary(pedestrainsummary t, pedestrainevent traffic, boolean hourly) {

		if (traffic.getContent().length > 0) {

			t.setLocationUid(traffic.getContent()[0].getLocationUid());

			for (int i = 0; i <= traffic.getContent().length - 1; i++) {
				t.setNoOfPeople(t.getNoOfPeople() + traffic.getContent()[i].getMeasures().getPedestrianCount());

				if (traffic.getContent()[i].getTimestamp() != null) {
					Date currentDate = new Date(Long.parseLong(traffic.getContent()[i].getTimestamp()));

					String d = hourly ? new SimpleDateFormat("MM/dd/YYYY hh a").format(currentDate).toString()
							: new SimpleDateFormat("MM/dd/YYYY").format(currentDate).toString();
					// System.out.println(d);
					if (t.getNumberOfCarsSpotted().containsKey(d)) {
						t.getNumberOfCarsSpotted().put(d, t.getNumberOfCarsSpotted().get(d)
								+ traffic.getContent()[i].getMeasures().getPedestrianCount());
					} else {
						t.getNumberOfCarsSpotted().put(d, traffic.getContent()[i].getMeasures().getPedestrianCount());
					}
				}
			}

		}

		return t;
	}

	public HttpEntity<String> getTrafficHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-TRAFFIC");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public ResponseEntity<parkingsummary> getPKINForLastTendays(@RequestParam String parking_loc,
			@RequestParam String startts, @RequestParam String endts, boolean hourly) {

		String starttime = getdateinTimeStamp(startts);
		String endtime = getdateinTimeStamp(endts);
		parkingsummary ps = new parkingsummary();
		try {
			while (Long.parseLong(starttime) <= Long.parseLong(endtime)) {

				parkingevent pkin = restTemplate.exchange(
						url + "locations/" + parking_loc + "/events?eventType=PKIN&" + "startTime=" + starttime
								+ "&endTime=" + String.valueOf(Long.parseLong(starttime) + 21600000),
						HttpMethod.GET, getParkingHeaders(), parkingevent.class).getBody();

				List<parkingDetails> objectuids = getobjectuids(pkin);

				parkingevent pkout = restTemplate.exchange(
						url + "locations/" + parking_loc + "/events?eventType=PKOUT&" + "startTime=" + starttime
								+ "&endTime=" + String.valueOf(Long.parseLong(starttime) + 21600000),
						HttpMethod.GET, getParkingHeaders(), parkingevent.class).getBody();

				objectuids = getparkingoutdetail(pkout, objectuids);

				ps = getparkingSummary(ps, objectuids, hourly);

				starttime = String.valueOf(Long.parseLong(starttime) + 21600000);
				// System.out.println("I am here");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new ResponseEntity<parkingsummary>(ps, getResponseHeaders(), HttpStatus.CREATED);
	}

	public void downloadparkingdetailcsv(List<parkingDetails> objectuids) {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("src/main/files/parkingdetail.csv", true);
			for (parkingDetails p : objectuids) {
				if (p.getTotaltime() != null) {
					fileWriter.append(String.valueOf(p.getAssetUid()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getXcoordinate(p.getCoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getYcoordinate(p.getCoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getLocationUid()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getParkingintimestamp()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getParkingouttimestamp()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getTotaltime()));
					fileWriter.append(",");
					String violation = Long.parseLong(p.getTotaltime()) > 7200000 ? "1" : "0";
					fileWriter.append(violation);
					fileWriter.append("\n");
				}
				// System.out.println("CSV file was created successfully !!!");

			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}

	public void downloadparkingsummarycsv(List<parkingsummary> objectuids) {

		FileWriter fileWriter = null;
		try {

			fileWriter = new FileWriter("src/main/files/parkingsummary.csv", true);
			for (parkingsummary p : objectuids) {

				p.getNumberOfCarsParked();

				for (Map.Entry<String, Integer> e : p.getNumberOfCarsParked().entrySet()) {

					fileWriter.append(String.valueOf(getdateinTimeStamp2(e.getKey())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(e.getKey()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(e.getValue()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getXcoordinate(p.getLocationcoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getYcoordinate(p.getLocationcoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getLocationUid()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getTotalNumberOfCars()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getTimeOverTwoHrs()));
					fileWriter.append("\n");

				}

			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}

	public void downloadtrafficsummarycsv(List<trafficsummary> objectuids) {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("src/main/files/trafficsummary.csv", true);
			for (trafficsummary p : objectuids) {

				for (Map.Entry<String, Integer> e : p.getNumberOfCarsSpotted().entrySet()) {

					fileWriter.append(String.valueOf(p.getNoOfVehivles()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getXcoordinate(p.getLocationcoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getYcoordinate(p.getLocationcoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getLocationUid()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(e.getKey()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(e.getKey()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(e.getValue()));
					fileWriter.append(",");

					fileWriter.append("\n");

					// System.out.println("CSV file was created successfully !!!");
				}
			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}

	public void downloadpedestrainsummarycsv(List<pedestrainsummary> objectuids) {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("src/main/files/pedestrainsummary.csv", true);
			for (pedestrainsummary p : objectuids) {

				for (Map.Entry<String, Integer> e : p.getNumberOfCarsSpotted().entrySet()) {

					fileWriter.append(String.valueOf(p.getNoOfPeople()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getXcoordinate(p.getLocationcoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(getYcoordinate(p.getLocationcoordinates())));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(p.getLocationUid()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(e.getKey()));
					fileWriter.append(",");
					fileWriter.append(String.valueOf(e.getValue()));
					fileWriter.append(",");

					fileWriter.append("\n");

					// System.out.println("CSV file was created successfully !!!");
				}
			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}
	}

	public parkingsummary getparkingSummary(parkingsummary parkingsummary, List<parkingDetails> objectuids,
			boolean hourly) {

		// downloadcsv(objectuids);

		parkingsummary.setTotalNumberOfCars(objectuids.size());
		if (objectuids.size() > 0) {
			parkingsummary.setLocationcoordinates(objectuids.get(0).getCoordinates());
			parkingsummary.setLocationUid(objectuids.get(0).getLocationUid());
		}

		for (parkingDetails p : objectuids) {
			if (p.getTotaltime() != null) {
				if (Long.parseLong(p.getTotaltime()) > 7200000) {
					parkingsummary.setTimeOverTwoHrs(parkingsummary.getTimeOverTwoHrs() + 1);
				}

				if (p.getParkingintimestamp() != null) {
					Date currentDate = new Date(Long.parseLong(p.getParkingintimestamp()));
					String d = null;
					if (hourly) {
						d = new SimpleDateFormat("MM/dd/yyyy hh a").format(currentDate).toString();
					} else {
						d = new SimpleDateFormat("MM/dd/yyyy").format(currentDate).toString();
					}

					if (parkingsummary.getNumberOfCarsParked().containsKey(d)) {
						parkingsummary.getNumberOfCarsParked().put(d,
								parkingsummary.getNumberOfCarsParked().get(d) + 1);
					} else {
						parkingsummary.getNumberOfCarsParked().put(d, 1);

					}
				}

			}

		}

		return parkingsummary;
	}

	public float getXcoordinate(String s) {
		String[] tokens = s.split(",|[:]+");

		float x_coordinate = 0;
		for (int i = 0; i <= tokens.length - 1; i++) {
			if (i % 2 == 0) {
				x_coordinate += Float.parseFloat(tokens[i]);
			}
		}
		x_coordinate = x_coordinate / (tokens.length / 2);
		return x_coordinate;

	}

	public float getYcoordinate(String s) {
		String[] tokens = s.split(",|[:]+");
		float y_coordinate = 0;
		for (int i = 0; i <= tokens.length - 1; i++) {
			if (i % 2 != 0) {
				y_coordinate += Float.parseFloat(tokens[i]);
			}
		}
		y_coordinate = y_coordinate / (tokens.length / 2);
		return y_coordinate;
	}

	public String getdateinTimeStamp(String date) {

		SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy");

		try {
			date = String.valueOf(d.parse(date).getTime());
			// System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(date);
		return date;
	}

	public String getdateinTimeStamp2(String date) {

		System.out.println(date);
		SimpleDateFormat d = new SimpleDateFormat("MM/dd/yyyy");

		try {
			date = String.valueOf(d.parse(date).getTime());
			// System.out.println(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(date);
		return date;
	}

	public HttpEntity<String> getParkingHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PARKING");
		// headers.set("Access-Control-Allow-Origin", "*");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public HttpHeaders getResponseHeaders() {
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		return responseHeaders;

	}

	public HttpEntity<String> getPedestrainHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + authorization);
		headers.set("Predix-Zone-Id", "SD-IE-PEDESTRIAN");
		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
		return entity;
	}

	public List<parkingDetails> getparkingoutdetail(parkingevent pe, List<parkingDetails> pk) {

		for (int i = 0; i <= pk.size() - 1; i++) {
			for (int j = 0; j <= pe.getContent().length - 1; j++) {
				if (pe.getContent()[j].getProperties().getObjectUid().equals(pk.get(i).getObjectUid())) {
					pk.get(i).setParkingouttimestamp(pe.getContent()[j].getTimestamp());

					String timeinmilliseconds = String.valueOf(Long.parseLong((pk.get(i).getParkingouttimestamp()))
							- Long.parseLong(pk.get(i).getParkingintimestamp()));

					pk.get(i).setTotaltime(String.valueOf(timeinmilliseconds));
					pe.getContent()[j].getProperties().setObjectUid("");
					break;
				}
			}

			/*
			 * objectDetails.add(new parkingDetails(pe.getContent()[i].getLocationUid(),
			 * pe.getContent()[i].getAssetUid(), pe.getContent()[i].getTimestamp(),
			 * pe.getContent()[i].getProperties().getObjectUid()));
			 */
		}
		return pk;

	}

	public List<parkingDetails> getobjectuids(parkingevent pe) {

		if (pe.getContent().length > 0) {
			pe.getContent()[0].getProperties().getGeoCoordinates();
		}

		List<parkingDetails> objectDetails = new ArrayList<parkingDetails>();
		for (int i = 0; i <= pe.getContent().length - 1; i++) {
			objectDetails.add(new parkingDetails(pe.getContent()[i].getLocationUid(), pe.getContent()[i].getAssetUid(),
					pe.getContent()[i].getTimestamp(), pe.getContent()[i].getProperties().getObjectUid(),
					pe.getContent()[i].getProperties().getGeoCoordinates()));
		}
		return objectDetails;
	}

	public void getParkingZonesLoactionscsv(locationDetail parkingDetail) {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("src/main/files/parkingzonedetail.csv", true);
			for (location p : parkingDetail.getContent()) {

				fileWriter.append(String.valueOf(getXcoordinate(p.getCoordinates())));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(getYcoordinate(p.getCoordinates())));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(p.getLocationUid()));
				fileWriter.append(",");
				fileWriter.append("\n");

				// System.out.println("CSV file was created successfully !!!");

			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}

	}
	
	
	
	public void getTrafficZonesLoactionscsv(locationDetail trafficDetail) {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("src/main/files/trafficzonedetail.csv", true);
			for (location p : trafficDetail.getContent()) {

				fileWriter.append(String.valueOf(getXcoordinate(p.getCoordinates())));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(getYcoordinate(p.getCoordinates())));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(p.getLocationUid()));
				fileWriter.append(",");
				fileWriter.append("\n");

				// System.out.println("CSV file was created successfully !!!");

			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}

	}
	
	
	public void getPedestrainZonesLoactionscsv(PedestrainDetail trafficDetail) {

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("src/main/files/pedestrainzonedetail.csv", true);
			for (location p : trafficDetail.getContent()) {

				fileWriter.append(String.valueOf(getXcoordinate(p.getCoordinates())));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(getYcoordinate(p.getCoordinates())));
				fileWriter.append(",");
				fileWriter.append(String.valueOf(p.getLocationUid()));
				fileWriter.append(",");
				fileWriter.append("\n");

				// System.out.println("CSV file was created successfully !!!");

			}
		} catch (Exception e) {
			// System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		} finally {

			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter !!!");
				e.printStackTrace();
			}

		}

	}

}
