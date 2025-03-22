package com.br.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.events.model.Event;
import com.br.events.repository.EventRepo;

import java.util.*;

@Service
public class EventService {

	@Autowired
	private EventRepo eventRepo;
	
	public Event addNewEvent(Event event) {
		// generanting the prettyName
		event.setPrettyName(event.getTitle().toLowerCase().replaceAll(" ", "-"));
		return eventRepo.save(event);
	}
	
	public List<Event> getAllEvents() {
		return (List<Event>)eventRepo.findAll();
	}
	
	public Event getByPrettyName(String prettyName) {
		return eventRepo.findByPrettyName(prettyName);
	}
	
}
