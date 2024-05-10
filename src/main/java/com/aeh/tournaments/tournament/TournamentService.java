package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.competitors.CompetitorService;
import com.aeh.tournaments.duel.Branch;
import com.aeh.tournaments.duel.DuelDTO;
import com.aeh.tournaments.duel.DuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final DuelService duelService;
    private final CompetitorService competitorService;

    TournamentReadDTO createTournament(TournamentDTO tournamentDTO) {
        Tournament tournament = new Tournament();
        Set<CompetitorDTO> leftCompetitors = new HashSet<>();
        Set<CompetitorDTO> rightCompetitors = new HashSet<>();
        tournamentDTO.getCompetitors().forEach(competitorDTO -> {
            if (leftCompetitors.size() < tournamentDTO.getCompetitors().size()/2 + tournamentDTO.getCompetitors().size()/2%2) {
                leftCompetitors.add(competitorDTO);
            } else {
                rightCompetitors.add(competitorDTO);
            }
        });
        Set<DuelDTO> duelsLeft = duelService.prepareRound(leftCompetitors, 1, Branch.LEFT);
        Set<DuelDTO> duelsRight = duelService.prepareRound(rightCompetitors, 1, Branch.RIGHT);
        Set<DuelDTO> duels = new HashSet<>();
        duels.addAll(duelsLeft);
        duels.addAll(duelsRight);
        tournament.setDuels(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
        tournament.setNumberOfCompetitors(tournamentDTO.getCompetitors().size());
        return TournamentReadDTO.toDto(tournamentRepository.save(tournament));
    }

    public CompetitorDTO getWinner(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        return CompetitorDTO.toDto(tournament.getWinner());
    }

    public Set<DuelDTO> getDuels(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        return tournament.getDuels().stream().map(DuelDTO::toDto).collect(Collectors.toSet());
    }

    Optional<TournamentReadDTO> getTournamentById(long tournamentId) {
        return tournamentRepository.findById(tournamentId).map(TournamentReadDTO::toDto);
    }

    TournamentReadDTO newRound(long tournamentId, int round) {
        Tournament tournament = tournamentRepository.getReferenceById(tournamentId);
        Set<CompetitorDTO> leftBranchCompetitors = tournament.getDuels().stream()
                .filter(duel -> duel.getBranch() == Branch.LEFT && duel.getRound() == (round - 1))
                .map(duel -> competitorService.getCompetitorById(duel.getWinner()))
                .collect(Collectors.toSet());
        Set<CompetitorDTO> rightBranchCompetitors = tournament.getDuels().stream()
                .filter(duel -> duel.getBranch() == Branch.RIGHT && duel.getRound() == (round - 1))
                .map(duel -> competitorService.getCompetitorById(duel.getWinner()))
                .collect(Collectors.toSet());
        Set<DuelDTO> duelsLeft = duelService.prepareRound(leftBranchCompetitors, round, Branch.LEFT);
        Set<DuelDTO> duelsRight = duelService.prepareRound(rightBranchCompetitors, round, Branch.RIGHT);
        Set<DuelDTO> duels = new HashSet<>();
        duels.addAll(duelsLeft);
        duels.addAll(duelsRight);
        tournament.getDuels().addAll(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
        return TournamentReadDTO.toDto(tournamentRepository.save(tournament));
    }
}
