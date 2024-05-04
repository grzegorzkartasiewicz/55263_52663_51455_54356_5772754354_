package com.aeh.tournaments.duel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


interface DuelRepository extends JpaRepository<Duel, Long> {
    List<Duel> findByCategory(String category);
}
