package com.aeh.tournaments.competitors;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CompetitorService {

    private final CompetitorRepository competitorRepository;

    CompetitorService(CompetitorRepository competitorRepository) {
        this.competitorRepository = competitorRepository;
    }

    CompetitorDTO save(CompetitorDTO competitor) {
        return CompetitorDTO.toDto(competitorRepository.save(competitor.toEntity()));
    }

    List<CompetitorDTO> getAllCompetitors() {
        return competitorRepository.findAll().stream().map(CompetitorDTO::toDto).toList();
    }

    CompetitorDTO update(CompetitorDTO competitor) {
        return CompetitorDTO.toDto(competitorRepository.save(competitor.toEntity()));
    }

    void delete(long competitorId) {
        competitorRepository.deleteById(competitorId);
    }
}
