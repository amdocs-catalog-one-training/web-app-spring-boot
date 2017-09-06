package com.amdocs.learning.controllers;

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
import com.fasterxml.jackson.core.JsonProcessingException;
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
		ResponseEntity<String> entity = null;
		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			request = new HttpEntity<String>(mapper.writeValueAsString(regform), headers);
		} catch (JsonProcessingException e1) {
			System.out.println(request.getBody());
			System.out.println(e1.getMessage());
			// return new ModelAndView("error");
		}
		RestTemplate restTemplate = new RestTemplate();
		try {
			entity = restTemplate.postForObject("http://localhost:9998/api/register/", request, ResponseEntity.class);
			// System.out.println(entity.getBody());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return new ModelAndView("index");
	}
}
