package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.Competitor;
import com.aeh.tournaments.duel.Duel;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
class TournamentService {

    private final TournamentRepository tournamentRepository;

    TournamentService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    Tournament createTournament(Tournament tournament) {
        return tournamentRepository.save(tournament);
    }

    Competitor getWinner(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        return tournament.getWinner();
    }

    Set<Duel> getDuels(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        return tournament.getDuels();
    }

    Optional<Tournament> getTournamentById(long tournamentId) {
        return tournamentRepository.findById(tournamentId);
    }

}
