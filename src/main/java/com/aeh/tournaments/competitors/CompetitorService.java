package com.aeh.tournaments.competitors;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CompetitorService {

    private final CompetitorRepository competitorRepository;

    CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    Competitor save(Competitor competitor) {
        return competitorRepository.save(competitor);
    }

    List<Competitor> getAllCompetitors() {
        return competitorRepository.findAll();
    }

    Competitor update(Competitor competitor) {
        return competitorRepository.save(competitor);
    }

    void delete(long competitorId) {
        competitorRepository.deleteById(competitorId);
    }
}
