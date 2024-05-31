package com.aeh.tournaments.competitors;

import com.aeh.tournaments.tournament.TournamentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = CompetitorController.INTERFACE_COMPETITORS)
@RequiredArgsConstructor
class CompetitorController {


    public static final String INTERFACE_COMPETITORS = "/competitors";

    private final CompetitorService competitorService;
    private final TournamentService tournamentService;

    /**
     * @param competitor that needs to be created
     * @return created competitor
     */
    @CrossOrigin
    @PostMapping
    ResponseEntity<CompetitorDTO> createCompetitor(@RequestBody @Valid CompetitorDTO competitor) {
        return ResponseEntity.ok(competitorService.save(competitor));
    }

    @CrossOrigin
    @PostMapping("/bulk")
    ResponseEntity<CompetitorDTO> createCompetitor(@RequestBody @Valid Set<CompetitorDTO> competitors) {
        competitors.forEach(competitorService::save);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @GetMapping("/{competitorId}")
    ResponseEntity<CompetitorDTO> getCompetitor(@PathVariable long competitorId) {
        return ResponseEntity.ok(competitorService.getCompetitorById(competitorId));
    }

    @CrossOrigin
    @GetMapping
    ResponseEntity<List<CompetitorDTO>> getAllCompetitors() {
        return ResponseEntity.ok(competitorService.getAllCompetitors());
    }
    @CrossOrigin

    @PutMapping(value = "/{competitorId}")
    ResponseEntity<CompetitorDTO> updateCompetitor(@PathVariable Long competitorId, @RequestBody @Valid CompetitorDTO competitor) {
        competitor.setId(competitorId);
        return ResponseEntity.ok(competitorService.update(competitor));
    }

    @CrossOrigin
    @DeleteMapping(value = "/{competitorId}")
    ResponseEntity<Object> deleteCompetitor(@PathVariable Long competitorId) {
        competitorService.delete(competitorId);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin
    @GetMapping("/tournaments/{tournamentId}")
    ResponseEntity<CompetitorDTO> getWinnerByTournamentId(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getWinner(tournamentId));
    }
}
