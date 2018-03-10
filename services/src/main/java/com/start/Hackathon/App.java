package com.start.Hackathon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;



@SpringBootApplication
@ComponentScan({"com.start.Hackathon"})
public class App implements CommandLineRunner{
	
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}





@Override
public void run(String... args) throws Exception {
	//Cards cards = cardsclient.getContract(1);
//	company c = cardsclient.getcompany();
	
	//System.out.println(c.getLocation());
	
	logger.info("Response: {}");
}

}
