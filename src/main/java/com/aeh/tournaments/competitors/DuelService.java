package com.aeh.tournaments.competitors;


import com.aeh.tournaments.competitors.model.Duel;
import com.aeh.tournaments.competitors.repository.DuelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DuelService {

    private final DuelRepository duelRepository;

    public DuelService(DuelRepository duelRepository) {
        this.duelRepository = duelRepository;
    }

    public List<Duel> getAllDuels() {
        return duelRepository.findAll();
    }

    public Duel getDuelById(Long duelId) {
        Optional<Duel> optionalDuel = duelRepository.findById(duelId);
        return optionalDuel.orElse(null);
    }

    public Duel save(Duel duel) {
        return duelRepository.save(duel);
    }

    public Optional<Duel> findById(Long duelId) {
        return duelRepository.findById(duelId);
    }

    public void deleteById(Long duelId) {
        duelRepository.deleteById(duelId);
    }


}


