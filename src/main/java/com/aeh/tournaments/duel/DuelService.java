package com.aeh.tournaments.duel;


import com.aeh.tournaments.competitors.Competitor;
import com.aeh.tournaments.competitors.CompetitorDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DuelService {

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

    public Set<DuelDTO> prepareFirstRound(Set<CompetitorDTO> competitors) {
        List<CompetitorDTO> competitorsList = new ArrayList<>(competitors);
        Set<DuelDTO> firstRound = new HashSet<>();
        int position = 0;
        for( int i = 0; i < competitors.size()/2; i=+2, position++) {
            DuelDTO duel = new DuelDTO();
            duel.setParticipant1(String.valueOf(competitorsList.get(i).getId()));
            duel.setParticipant2(String.valueOf((i + 1) == competitorsList.size() ? null : competitorsList.get(i + 1).getId()));
            duel.setPosition(position);
            firstRound.add(duel);
        }
        return firstRound;
    }
}


