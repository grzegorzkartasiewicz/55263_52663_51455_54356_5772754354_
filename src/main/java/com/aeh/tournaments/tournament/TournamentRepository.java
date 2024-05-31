package com.aeh.tournaments.tournament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
