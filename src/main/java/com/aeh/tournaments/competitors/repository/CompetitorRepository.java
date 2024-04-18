package com.aeh.tournaments.competitors.repository;

import com.aeh.tournaments.competitors.model.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompetitorRepository extends JpaRepository<Competitor, Integer> {
}
