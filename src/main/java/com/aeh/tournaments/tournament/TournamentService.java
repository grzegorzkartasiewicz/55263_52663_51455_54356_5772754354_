package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.Competitor;
import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.duel.Duel;
import com.aeh.tournaments.duel.DuelDTO;
import com.aeh.tournaments.duel.DuelService;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final DuelService duelService;

    TournamentService(TournamentRepository tournamentRepository, DuelService duelService) {
        this.tournamentRepository = tournamentRepository;
        this.duelService = duelService;
    }

    TournamentReadDTO createTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = new Tournament();
        Set<DuelDTO> duels = duelService.prepareFirstRound(tournamentDTO.getCompetitors());
        tournament.setDuels(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
        tournament.setNumberOfCompetitors(tournamentDTO.getCompetitors().size());
        return TournamentReadDTO.toDto(tournamentRepository.save(tournament));
    }

    CompetitorDTO getWinner(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        return CompetitorDTO.toDto(tournament.getWinner());
    }

    Set<DuelDTO> getDuels(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        return tournament.getDuels().stream().map(DuelDTO::toDto).collect(Collectors.toSet());
    }

    Optional<TournamentReadDTO> getTournamentById(long tournamentId) {
        return tournamentRepository.findById(tournamentId).map(TournamentReadDTO::toDto);
    }

}
