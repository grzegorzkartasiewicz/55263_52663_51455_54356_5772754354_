package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.competitors.CompetitorService;
import com.aeh.tournaments.duel.Branch;
import com.aeh.tournaments.duel.Duel;
import com.aeh.tournaments.duel.DuelDTO;
import com.aeh.tournaments.duel.DuelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository tournamentRepository;
    private final DuelService duelService;
    private final CompetitorService competitorService;

    TournamentRoundResponse createFirstRound(Tournament tournament) {
        Set<CompetitorDTO> leftCompetitors = new HashSet<>();
        Set<CompetitorDTO> rightCompetitors = new HashSet<>();
        tournament.getCompetitors().forEach(competitor -> {
            if (leftCompetitors.size() < tournament.getCompetitors().size()/2 + tournament.getCompetitors().size()/2%2) {
                leftCompetitors.add(CompetitorDTO.toDto(competitor));
            } else {
                rightCompetitors.add(CompetitorDTO.toDto(competitor));
            }
        });
        Set<DuelDTO> duelsLeft = duelService.prepareRound(leftCompetitors, 1, Branch.LEFT);
        Set<DuelDTO> duelsRight = duelService.prepareRound(rightCompetitors, 1, Branch.RIGHT);
        Set<DuelDTO> duels = new HashSet<>();
        duels.addAll(duelsLeft);
        duels.addAll(duelsRight);
        tournament.setDuels(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
        tournament.setNumberOfCompetitors(tournament.getCompetitors().size());
        return TournamentRoundResponse.toDtoInRound(tournamentRepository.save(tournament), 1);
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

    TournamentRoundResponse newRound(long tournamentId, int round) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        if (tournament.getDuels().isEmpty()) {
            return createFirstRound(tournament);
        } else {
            Set<CompetitorDTO> leftBranchCompetitors = tournament.getDuels().stream()
                    .filter(duel -> duel.getBranch() == Branch.LEFT && duel.getRound() == (round - 1))
                    .map(duel -> competitorService.getCompetitorById(duel.getWinner()))
                    .collect(Collectors.toSet());
            Set<CompetitorDTO> rightBranchCompetitors = tournament.getDuels().stream()
                    .filter(duel -> duel.getBranch() == Branch.RIGHT && duel.getRound() == (round - 1))
                    .map(duel -> competitorService.getCompetitorById(duel.getWinner()))
                    .collect(Collectors.toSet());
            if (leftBranchCompetitors.size() == 1 && rightBranchCompetitors.size() == 1) {
                List<CompetitorDTO> finalists = new ArrayList<>();
                finalists.addAll(leftBranchCompetitors);
                finalists.addAll(rightBranchCompetitors);
                tournament.getDuels().addAll(duelService.prepareFinal(finalists, round).stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
                tournament.setNumberOfCompetitors(2);
            } else {
                Set<DuelDTO> duelsLeft = duelService.prepareRound(leftBranchCompetitors, round, Branch.LEFT);
                Set<DuelDTO> duelsRight = duelService.prepareRound(rightBranchCompetitors, round, Branch.RIGHT);
                Set<DuelDTO> duels = new HashSet<>();
                duels.addAll(duelsLeft);
                duels.addAll(duelsRight);
                tournament.getDuels().addAll(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
                tournament.setNumberOfCompetitors(countCompetitors(duels));
            }
        }
        return TournamentRoundResponse.toDtoInRound(tournamentRepository.save(tournament), round);
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

    public TournamentRoundResponse createLosingBranches(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        Duel finalDuel = tournament.getDuels().stream().max(Comparator.comparingInt(Duel::getRound)).orElseThrow();
        Set<CompetitorDTO> leftBranchCompetitors = tournament.getDuels().stream().filter(duel -> !Branch.FINAL.equals(duel.getBranch())).filter(duel -> Objects.equals(duel.getParticipant1(), finalDuel.getParticipant1())
                        || Objects.equals(duel.getParticipant2(), finalDuel.getParticipant1()))
                .map(duel -> {
                    if (finalDuel.getParticipant1().equals(duel.getParticipant1()) && duel.getParticipant2() != null) {
                        return competitorService.getCompetitorById(duel.getParticipant2());
                    } else if (finalDuel.getParticipant1().equals(duel.getParticipant2())) {
                        return competitorService.getCompetitorById(duel.getParticipant1());
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toSet());
        Set<CompetitorDTO> rightBranchCompetitors = tournament.getDuels().stream().filter(duel -> !Branch.FINAL.equals(duel.getBranch())).filter(duel -> Objects.equals(duel.getParticipant1(), finalDuel.getParticipant2())
                        || Objects.equals(duel.getParticipant2(), finalDuel.getParticipant2()))
                .map(duel -> {
                    if (finalDuel.getParticipant2().equals(duel.getParticipant1()) && duel.getParticipant2() != null) {
                        return competitorService.getCompetitorById(duel.getParticipant2());
                    } else if (finalDuel.getParticipant2().equals(duel.getParticipant2())) {
                        return competitorService.getCompetitorById(duel.getParticipant1());
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toSet());
        int round = finalDuel.getRound() + 1;
        Set<DuelDTO> duelsLeft = duelService.prepareRound(leftBranchCompetitors, round, Branch.LEFT);
        Set<DuelDTO> duelsRight = duelService.prepareRound(rightBranchCompetitors, round, Branch.RIGHT);
        Set<DuelDTO> duels = new HashSet<>();
        duels.addAll(duelsLeft);
        duels.addAll(duelsRight);
        tournament.getDuels().addAll(duels.stream().map(DuelDTO::toEntity).collect(Collectors.toSet()));
        tournament.setNumberOfCompetitors(countCompetitors(duels));
        return TournamentRoundResponse.toDtoInRound(tournamentRepository.save(tournament), round);
    }

    public TournamentController.TournamentPodiumDTO getPodium(long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new NoSuchElementException("Tournament not found"));
        List<Duel> duels = tournament.getDuels().stream().sorted(Comparator.comparing(Duel::getRound).reversed()).toList();

        Duel finalDuel = duels.stream()
                .filter(duel -> Branch.FINAL.equals(duel.getBranch()))
                .findFirst()
                .orElseThrow();

        CompetitorDTO firstPlace = competitorService.getCompetitorById(finalDuel.getWinner());

        Long secondPlaceId = finalDuel.getWinner().equals(finalDuel.getParticipant1())
                ? finalDuel.getParticipant2()
                : finalDuel.getParticipant1();
        CompetitorDTO secondPlace = competitorService.getCompetitorById(secondPlaceId);

        Duel thirdPlace1 = duels.get(0);
        Duel thirdPlace2 = duels.get(1);
        CompetitorDTO thirdPlaceLeft;
        CompetitorDTO thirdPlaceRight;
        if (Branch.LEFT.equals(thirdPlace1.getBranch())) {
            thirdPlaceLeft = competitorService.getCompetitorById(thirdPlace1.getWinner());
            thirdPlaceRight = competitorService.getCompetitorById(thirdPlace2.getWinner());
        } else {
            thirdPlaceLeft = competitorService.getCompetitorById(thirdPlace2.getWinner());
            thirdPlaceRight = competitorService.getCompetitorById(thirdPlace1.getWinner());
        }

        return new TournamentController.TournamentPodiumDTO(firstPlace, secondPlace, thirdPlaceLeft, thirdPlaceRight);
    }

    public CompetitorDTO addCompetitor(long tournamentId, long competitorId) {
        if (tournamentRepository.findAll().isEmpty()) {
            createTournament(new TournamentController.CreateTournamentRequest("Mistrzostwa Warszawy Karate", "15.06.2024", 10, "Karate", "test@test.com"));
        }
        Tournament tournament = tournamentRepository.findById(tournamentId).orElseThrow();
        CompetitorDTO competitorDTO = competitorService.getCompetitorById(competitorId);
        tournament.getCompetitors().add(competitorDTO.toEntity());

        tournamentRepository.save(tournament);
        return competitorDTO;
    }

    public TournamentController.CreateTournamentResponse createTournament(TournamentController.CreateTournamentRequest createTournamentRequest) {
        Tournament tournament = new Tournament();
        tournament.setTournamentName(createTournamentRequest.name());
        tournament.setTournamentData(createTournamentRequest.data());
        tournament.setNumberOfCompetitors(createTournamentRequest.numberOfCompetitors());
        tournament.setCategory(createTournamentRequest.category());
        tournament.setEmail(createTournamentRequest.email());

        tournament = tournamentRepository.save(tournament);
        return new TournamentController.CreateTournamentResponse(tournament.getId(), tournament.getTournamentName(),
                tournament.getTournamentData(), tournament.getNumberOfCompetitors(), tournament.getCategory(), tournament.getEmail());
    }
}
