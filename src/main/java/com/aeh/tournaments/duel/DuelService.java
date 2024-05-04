package com.aeh.tournaments.duel;


import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
class DuelService {

    private final DuelRepository duelRepository;

    DuelService(DuelRepository duelRepository) {
        this.duelRepository = duelRepository;
    }

    List<Duel> getAllDuels() {
        return duelRepository.findAll();
    }

    Duel getDuelById(Long duelId) {
        Optional<Duel> optionalDuel = duelRepository.findById(duelId);
        return optionalDuel.orElse(null);
    }

    Duel save(Duel duel) {
        return duelRepository.save(duel);
    }

    Optional<Duel> findById(Long duelId) {
        return duelRepository.findById(duelId);
    }

    void deleteById(Long duelId) {
        duelRepository.deleteById(duelId);
    }


    List<Duel> findByCategory(String category) {
        return duelRepository.findByCategory(category);
    }
}


