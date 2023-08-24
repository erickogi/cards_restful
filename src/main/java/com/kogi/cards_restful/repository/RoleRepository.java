package com.kogi.cards_restful.repository;

import com.kogi.cards_restful.models.ERole;
import com.kogi.cards_restful.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
