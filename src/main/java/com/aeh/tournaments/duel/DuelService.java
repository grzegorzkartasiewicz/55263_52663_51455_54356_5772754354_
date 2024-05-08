package com.aeh.tournaments.duel;


import com.aeh.tournaments.competitors.CompetitorDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DuelService {

    public static final String PARTICIPANT_1 = "participant1";
    public static final String PARTICIPANT_2 = "participant2";
    private final DuelRepository duelRepository;

    DuelService(DuelRepository duelRepository) {
        this.duelRepository = duelRepository;
    }

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


    List<DuelDTO> findByCategory(String category) {
        return duelRepository.findByCategory(category).stream().map(DuelDTO::toDto).toList();
    }

    public Set<DuelDTO> prepareRound(Set<CompetitorDTO> competitors) {
        Set<DuelDTO> round = new HashSet<>();

        Map<String, List<CompetitorDTO>> duelDraft = new HashMap<>();
        duelDraft.put(PARTICIPANT_1, new ArrayList<>());
        duelDraft.put(PARTICIPANT_2, new ArrayList<>());
        competitors.stream()
                .sorted(Comparator.comparing(CompetitorDTO::getClub)).forEach(competitorDTO -> {
                    if (duelDraft.get(PARTICIPANT_1).size() < competitors.size()/2) {
                        duelDraft.get(PARTICIPANT_1).add(competitorDTO);
                    } else {
                        duelDraft.get(PARTICIPANT_2).add(competitorDTO);
                    }
                });
        List<CompetitorDTO> participant1 = duelDraft.get(PARTICIPANT_1);
        List<CompetitorDTO> participant2 = duelDraft.get(PARTICIPANT_2);
        for (int i = 0; i < participant1.size(); i++) {
            DuelDTO duel = new DuelDTO();
            duel.setPosition(i);
            duel.setParticipant1(participant1.get(i).getId());
            duel.setParticipant2(participant2.get(i) == null ? null : participant2.get(i).getId());
            round.add(duel);
        }
        return round;
    }
}


