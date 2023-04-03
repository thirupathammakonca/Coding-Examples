package com.thiruacademy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClient {
	static String baseUrl = "http://localhost:8090/products/fetchAll";
	
	@Autowired
	private static RestTemplate restTemplate;
	
	static List<ProductResponse> responseList = null;
	
	public static List<ProductResponse> getProductDetails() {
		restTemplate = new RestTemplate();
		// Set the basic auth credentials
		/*
		 * String plainCreds = "Thiru:Thiru"; byte[] plainCredsBytes =
		 * plainCreds.getBytes(); byte[] base64CredsBytes =
		 * Base64.getEncoder().encode(plainCredsBytes); String base64Creds = new
		 * String(base64CredsBytes); HttpHeaders headers = new HttpHeaders();
		 * headers.set("Authorization", "Basic" + base64Creds);
		 */
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		//headers.setBasicAuth(baseUrl);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		String response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class).getBody();
		System.out.println(response);
		List<ProductResponse> list = handleRequest(response);
		return list;
	}
	public static List<ProductResponse> handleRequest(String response) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			responseList = new ArrayList<>();
			ProductResponse productResponse = null;
			List<ProductDetails> detailsList = mapper.readValue(response, new TypeReference<List<ProductDetails>>() {});
			for(ProductDetails details : detailsList) {
				productResponse = new ProductResponse();
				productResponse.setName(details.getName());
				productResponse.setDepartment(details.getDepartment());
				productResponse.setPrice(details.getPrice());
				System.out.println(productResponse);
				responseList.add(productResponse);
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return responseList; 
		
	}
}
