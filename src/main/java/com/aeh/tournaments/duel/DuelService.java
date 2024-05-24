package com.aeh.tournaments.duel;


import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.competitors.CompetitorService;
import com.aeh.tournaments.tournament.TournamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DuelService {

    public static final String PARTICIPANT_1 = "participant1";
    public static final String PARTICIPANT_2 = "participant2";
    private final DuelRepository duelRepository;
    private final CompetitorService competitorService;


    List<Duel> getAllDuels() {
        return duelRepository.findAll();
    }

    DuelDTO getDuelById(Long duelId) {
        Optional<Duel> optionalDuel = duelRepository.findById(duelId);
        return optionalDuel.map(DuelDTO::toDto).orElse(null);
    }

    DuelDTO save(DuelDTO duel) {
        return DuelDTO.toDto(duelRepository.save(duel.toEntity()));
    }

    Optional<DuelDTO> findById(Long duelId) {
        return duelRepository.findById(duelId).map(DuelDTO::toDto);
    }

    void deleteById(Long duelId) {
        duelRepository.deleteById(duelId);
    }

    public void updateWinner(long duelId, long winnerId) {
        Duel duel = duelRepository.findById(duelId).orElseThrow();
        duel.setWinner(winnerId);
        duelRepository.save(duel);
    }


    public Set<DuelDTO> prepareRound(Set<CompetitorDTO> competitors, int round, Branch branch) {
        Map<String, List<CompetitorDTO>> duelDraft = new HashMap<>();
        duelDraft.put(PARTICIPANT_1, new ArrayList<>());
        duelDraft.put(PARTICIPANT_2, new ArrayList<>());
        competitors.stream()
                .sorted(Comparator.comparing(CompetitorDTO::getClub)).forEach(competitorDTO -> {
                    if (duelDraft.get(PARTICIPANT_1).size() < competitors.size()/2 + competitors.size()%2) {
                        if (competitorDTO.isSkippedLast()) {
                            duelDraft.get(PARTICIPANT_1).add(0, competitorDTO);
                        } else {
                            duelDraft.get(PARTICIPANT_1).add(competitorDTO);
                        }
                    } else {
                        if (competitorDTO.isSkippedLast()) {
                            duelDraft.get(PARTICIPANT_2).add(0, competitorDTO);
                            competitorService.setSkippedLast(competitorDTO.getId());
                        } else {
                            duelDraft.get(PARTICIPANT_2).add(competitorDTO);
                        }
                    }
                });
        List<CompetitorDTO> participant1 = duelDraft.get(PARTICIPANT_1);
        List<CompetitorDTO> participant2 = duelDraft.get(PARTICIPANT_2);
        return setDuels(round, branch, participant1, participant2);
    }

    private Set<DuelDTO> setDuels(int round, Branch branch, List<CompetitorDTO> participant1, List<CompetitorDTO> participant2) {
        Set<DuelDTO> duels = new HashSet<>();
        for (int i = 0; i < participant1.size(); i++) {
            DuelDTO duel = new DuelDTO();
            duel.setPosition(i);
            duel.setRound(round);
            duel.setBranch(branch);
            duel.setParticipant1(participant1.get(i).getId());
            duel.setParticipant2(participant2.size() == i ? null : participant2.get(i).getId());
            if (duel.getParticipant2()==null) {
                competitorService.setSkippedLast(participant1.get(i).getId());
                duel.setWinner(participant1.get(i).getId());
            }
            duels.add(duel);
        }
        return duels;
    }

    public DuelDTO updateDuel(long duelId, DuelDTO updatedDuel) {
        DuelDTO existingDuel = findById(duelId).orElseThrow();
        existingDuel.setWinner(updatedDuel.getWinner());
        return DuelDTO.toDto(duelRepository.save(existingDuel.toEntity()));
    }

    public Set<DuelDTO> prepareFinal(List<CompetitorDTO> finalists, int round) {
        DuelDTO duel = new DuelDTO();
        duel.setPosition(0);
        duel.setRound(round);
        duel.setBranch(Branch.FINAL);
        duel.setParticipant1(finalists.get(0).getId());
        duel.setParticipant2(finalists.get(1).getId());
        return Set.of(duel);
    }
}


