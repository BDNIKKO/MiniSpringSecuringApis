package org.example.minispringsecuringapis.repository;

import org.example.minispringsecuringapis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotate it as a repository
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by their username
    User findByUsername(String username);
}
