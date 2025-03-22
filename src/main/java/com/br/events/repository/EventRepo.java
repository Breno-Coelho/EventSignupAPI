package com.br.events.repository;

import org.springframework.data.repository.CrudRepository;

import com.br.events.model.Event;

public interface EventRepo extends CrudRepository<Event, Integer>{
	public Event findByPrettyName(String prettyName);
}
