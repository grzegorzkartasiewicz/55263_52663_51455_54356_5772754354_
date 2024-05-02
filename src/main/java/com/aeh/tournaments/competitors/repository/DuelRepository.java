package com.aeh.tournaments.competitors.repository;

import java.util.List;

import com.aeh.tournaments.competitors.model.Duel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DuelRepository extends JpaRepository<Duel, Long> {
    List<Duel> findByCategory(String category);
}
