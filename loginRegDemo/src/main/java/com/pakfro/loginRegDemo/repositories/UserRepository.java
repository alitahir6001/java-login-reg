package com.pakfro.loginRegDemo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.pakfro.loginRegDemo.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);

}
