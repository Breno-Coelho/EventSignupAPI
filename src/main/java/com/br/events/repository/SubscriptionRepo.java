package com.br.events.repository;

import org.springframework.data.repository.CrudRepository;

import com.br.events.model.Event;
import com.br.events.model.Subscription;
import com.br.events.model.User;

public interface SubscriptionRepo extends CrudRepository<Subscription, Integer>{
	
	public Subscription findByEventAndSubscriber(Event evt, User user);
}
