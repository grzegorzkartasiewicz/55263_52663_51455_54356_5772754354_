package com.aeh.tournaments.competitors;

import com.aeh.tournaments.competitors.model.Competitor;
import com.aeh.tournaments.competitors.repository.CompetitorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitorService {

    private final CompetitorRepository competitorRepository;

    public CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    public Competitor save(Competitor competitor) {
        return competitorRepository.save(competitor);
    }

    public List<Competitor> getAllCompetitors() {
        return competitorRepository.findAll();
    }

    public Competitor update(Competitor competitor) {
        return competitorRepository.save(competitor);
    }

    public void delete(Integer competitorId) {
        competitorRepository.deleteById(competitorId);
    }
}
