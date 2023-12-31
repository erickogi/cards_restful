package com.kogi.cards_restful.repository;

import com.kogi.cards_restful.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String username);

  Boolean existsByEmail(String email);
}
