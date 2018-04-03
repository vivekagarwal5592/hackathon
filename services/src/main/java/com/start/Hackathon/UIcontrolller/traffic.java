package com.start.Hackathon.UIcontrolller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class traffic {

	
	@GetMapping("/")
	public String index() {
		return "index.html";
	}

	@GetMapping("/getparkinganalysis")
	public String getparkinganalysis() {
		//System.out.println("In pa");
		return "getparkinganalysis.html";
	}

	@GetMapping("/gettrafficanalysis")
	public String gettrafficanalysis() {
	//	System.out.println("In pa");
		return "gettrafficanalysis.html";
	}

	@GetMapping("/currentlocation")
	public String currentlocation() {
	//	System.out.println("In pa");
		return "currentlocation.html";
	}
	
	@GetMapping("/locationanalysis")
	public String getlocationanakysis() {
	//	System.out.println("In pa");
		return "LocationAnalysis.html";
	}

}
