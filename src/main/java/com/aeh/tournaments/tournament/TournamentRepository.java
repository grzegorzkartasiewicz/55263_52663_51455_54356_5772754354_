package com.aeh.tournaments.tournament;

import org.springframework.data.jpa.repository.JpaRepository;

interface TournamentRepository extends JpaRepository<Tournament, Long> {
}
