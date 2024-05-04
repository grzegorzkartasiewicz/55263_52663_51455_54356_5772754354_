package com.aeh.tournaments.competitors;

import org.springframework.data.jpa.repository.JpaRepository;

interface CompetitorRepository extends JpaRepository<Competitor, Long> {
}
