package com.br.events.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.events.dto.ErrorMessage;
import com.br.events.dto.SubscriptionConflictException;
import com.br.events.exception.EventNotFoundException;
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
		}
		return ResponseEntity.badRequest().build();
	}
}
