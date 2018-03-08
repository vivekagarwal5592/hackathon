package com.start.Hackathon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestConfig {

	
	@Bean
	public RestOperations createRestTemplate(final ClientHttpRequestFactory ClientHttpRequestFactory) {
		return new RestTemplate(ClientHttpRequestFactory);
	}
	
	
	@Bean
	public ClientHttpRequestFactory createClientHttpRequestFactory (@Value("${connect.timeout}") final int conntectTimeout,
			@Value("${read.timeout}") final int readTimeout	) {
	 HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory  = 
			 new HttpComponentsClientHttpRequestFactory();
	 httpComponentsClientHttpRequestFactory.setReadTimeout(readTimeout);
	 httpComponentsClientHttpRequestFactory.setConnectTimeout(conntectTimeout);
	 return httpComponentsClientHttpRequestFactory;
	 
	}
}
