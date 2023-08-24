package com.kogi.cards_restful.repository;

import com.kogi.cards_restful.models.Card;
import com.kogi.cards_restful.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends PagingAndSortingRepository<Card, Long>, JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    Page<Card> findByCreator(User user, Pageable pageable);
    Optional<Card> findByIdAndCreator(Long id,User user);
    Optional<Card> findById(Long id);
    Page<Card> findByCreator(User user,Specification <Card> finalSpec, Pageable pageable);
    Page<Card> findAll(Specification <Card> finalSpec,Pageable pageable);
}
