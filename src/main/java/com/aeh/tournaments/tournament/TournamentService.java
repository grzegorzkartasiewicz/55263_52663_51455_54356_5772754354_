package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.competitors.CompetitorService;
import com.aeh.tournaments.duel.Branch;
import com.aeh.tournaments.duel.Duel;
import com.aeh.tournaments.duel.DuelDTO;
import com.aeh.tournaments.duel.DuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import java.util.*;
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
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Set<CompetitorDTO> leftBranchCompetitors = tournament.getDuels().stream()
                .filter(duel -> duel.getBranch() == Branch.LEFT && duel.getRound() == (round - 1))
                .map(duel -> competitorService.getCompetitorById(duel.getWinner()))
                .collect(Collectors.toSet());
        Set<CompetitorDTO> rightBranchCompetitors = tournament.getDuels().stream()
                .filter(duel -> duel.getBranch() == Branch.RIGHT && duel.getRound() == (round - 1))
                .map(duel -> competitorService.getCompetitorById(duel.getWinner()))
                .collect(Collectors.toSet());
        if (tournament.getNumberOfCompetitors() == 2) {
            List<CompetitorDTO> finalists = new ArrayList<>();
            finalists.addAll(leftBranchCompetitors);
            finalists.addAll(rightBranchCompetitors);
            tournament.getDuels().addAll(duelService.prepareFinal(finalists, round).stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
            tournament.setNumberOfCompetitors(0);
        } else {
            Set<DuelDTO> duelsLeft = duelService.prepareRound(leftBranchCompetitors, round, Branch.LEFT);
            Set<DuelDTO> duelsRight = duelService.prepareRound(rightBranchCompetitors, round, Branch.RIGHT);
            Set<DuelDTO> duels = new HashSet<>();
            duels.addAll(duelsLeft);
            duels.addAll(duelsRight);
            tournament.getDuels().addAll(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
            tournament.setNumberOfCompetitors(countCompetitors(duels));
        }
        return TournamentReadDTO.toDto(tournamentRepository.save(tournament));
    }

    private int countCompetitors(Set<DuelDTO> duels) {
        return duels.stream().map(duelDTO -> {
            if (duelDTO.getParticipant2() == null) {
                return 1;
            } else {
                return 2;
            }
        }).mapToInt(Integer::intValue).sum();
    }

    public TournamentReadDTO createLosingBranches(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Duel finalDuel = tournament.getDuels().stream().max(Comparator.comparingInt(Duel::getRound)).orElseThrow();
        Set<CompetitorDTO> leftBranchCompetitors = tournament.getDuels().stream().filter(duel -> Objects.equals(duel.getParticipant1(), finalDuel.getParticipant1())
                        || Objects.equals(duel.getParticipant2(), finalDuel.getParticipant1()))
                .map(duel -> {
                    if (finalDuel.getParticipant1().equals(duel.getParticipant1()) && duel.getParticipant2() != null) {
                        return competitorService.getCompetitorById(duel.getParticipant2());
                    } else if (finalDuel.getParticipant1().equals(duel.getParticipant2())) {
                        return competitorService.getCompetitorById(duel.getParticipant1());
                    }
                    return null;
                }).collect(Collectors.toSet());
        Set<CompetitorDTO> rightBranchCompetitors = tournament.getDuels().stream().filter(duel -> Objects.equals(duel.getParticipant1(), finalDuel.getParticipant2())
                        || Objects.equals(duel.getParticipant2(), finalDuel.getParticipant2()))
                .map(duel -> {
                    if (finalDuel.getParticipant2().equals(duel.getParticipant1()) && duel.getParticipant2() != null) {
                        return competitorService.getCompetitorById(duel.getParticipant2());
                    } else if (finalDuel.getParticipant2().equals(duel.getParticipant2())) {
                        return competitorService.getCompetitorById(duel.getParticipant1());
                    }
                    return null;
                }).collect(Collectors.toSet());
        int round = finalDuel.getRound() + 1;
        Set<DuelDTO> duelsLeft = duelService.prepareRound(leftBranchCompetitors, round, Branch.LEFT);
        Set<DuelDTO> duelsRight = duelService.prepareRound(rightBranchCompetitors, round, Branch.RIGHT);
        Set<DuelDTO> duels = new HashSet<>();
        duels.addAll(duelsLeft);
        duels.addAll(duelsRight);
        tournament.getDuels().addAll(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
        tournament.setNumberOfCompetitors(countCompetitors(duels));
        return TournamentReadDTO.toDto(tournamentRepository.save(tournament));
    }
}
