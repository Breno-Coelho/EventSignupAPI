package com.br.events.repository;

import org.springframework.data.repository.CrudRepository;

import com.br.events.model.User;

public interface UserRepo extends CrudRepository<User, Integer> {
	public User findByEmail(String email);
}
