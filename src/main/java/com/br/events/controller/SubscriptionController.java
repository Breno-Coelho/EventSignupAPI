package com.br.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.events.dto.ErrorMessage;
import com.br.events.exception.EventNotFoundException;
import com.br.events.exception.SubscriptionConflictException;
import com.br.events.exception.UserIndicatorNotFoundException;
import com.br.events.model.Subscription;
import com.br.events.model.User;
import com.br.events.service.SubscriptionService;

@RestController
public class SubscriptionController {

	@Autowired
	private SubscriptionService service;

	@PostMapping({"/subscription/{prettyname}", "/subscription/{prettyname}/{userId}"})
	public ResponseEntity<?> createSubscription(
		@PathVariable String prettyName, 
		@RequestBody User subscriber,
		@PathVariable(required = false) Integer userId
	) {
		try {
			Subscription res = service.CreateNewSubscription(prettyName, subscriber, userId);
			if (res != null) {
				return ResponseEntity.ok(res);
			}
		} catch (EventNotFoundException e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		} catch (SubscriptionConflictException e) {
			return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));
		} catch (UserIndicatorNotFoundException e) {
			return ResponseEntity.status(409).body(new ErrorMessage(e.getMessage()));
		}
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/subscription/{prettyName}/ranking")
	public ResponseEntity<?> generateRankingByEvent(@PathVariable String prettyName) {
		try {
			return ResponseEntity.ok(service.getCompleteRanking(prettyName).subList(0, 3));
		} catch (EventNotFoundException e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		}
	}
	
	@GetMapping("/subscription/{prettyName}/ranking/{userId}")
	public ResponseEntity<?> generateRankingByEventAndUser(
			@PathVariable String prettyName,
			@PathVariable Integer userId
	) {
		try {			
			service.getRankingByUser(prettyName, userId);
			return ResponseEntity.ok(service.getRankingByUser(prettyName, userId));
		} catch (Exception e) {
			return ResponseEntity.status(404).body(new ErrorMessage(e.getMessage()));
		} 
	}
}
