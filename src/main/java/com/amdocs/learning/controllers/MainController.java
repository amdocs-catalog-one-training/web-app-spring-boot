package com.amdocs.learning.controllers;

import java.awt.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.amdocs.learning.models.Regform;
import com.amdocs.learning.models.User;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {

	@RequestMapping("/")
	public ModelAndView index() {

		return new ModelAndView("index");
	}

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration(Model model) {
		return new ModelAndView("registration", "regform", new Regform());
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView registrationSubmit(@ModelAttribute Regform regform, Model model) {

		model.addAttribute("regform", regform);
		ObjectMapper mapper = new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			request = new HttpEntity<String>(mapper.writeValueAsString(regform), headers);
		} catch (JsonProcessingException e1) {
			return new ModelAndView("error");
		}
		User user = new User();
		RestTemplate restTemplate = new RestTemplate();
		try {
			user = restTemplate.postForObject("http://localhost:9998/api/register/", request, User.class);
		} catch (Exception e) {
			return new ModelAndView("error");
		}

		return new ModelAndView("index");
	}
	
	@RequestMapping("/show")
	public ModelAndView showAllUsers() {
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		ArrayList<User> users = new ArrayList<User>();
		String str = restTemplate.getForObject("http://localhost:9998/api/users/", String.class);
		ObjectMapper mapper = new ObjectMapper();
		
			try {
				users = mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(ArrayList.class, User.class));
			} catch (JsonParseException e) {
				return new ModelAndView("error");
			} catch (JsonMappingException e) {
				return new ModelAndView("error");
			} catch (IOException e) {
				return new ModelAndView("error");
			}
		
		return new ModelAndView("showall", "users", users);
	}
}
