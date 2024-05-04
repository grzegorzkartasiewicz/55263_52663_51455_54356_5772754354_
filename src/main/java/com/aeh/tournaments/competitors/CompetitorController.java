package com.aeh.tournaments.competitors;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = CompetitorController.INTERFACE_COMPETITORS)
class CompetitorController {


    public static final String INTERFACE_COMPETITORS = "/competitors";

    private final CompetitorService competitorService;

    CompetitorController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    /**
     * @param competitor that needs to be created
     * @return created competitor
     */
    @PostMapping
    ResponseEntity<CompetitorDTO> createCompetitor(@RequestBody @Valid CompetitorDTO competitor) {
        return ResponseEntity.ok(competitorService.save(competitor));
    }

    @GetMapping
    ResponseEntity<List<CompetitorDTO>> getAllCompetitors() {
        return ResponseEntity.ok(competitorService.getAllCompetitors());
    }

    @PutMapping(value = "/{competitorId}")
    ResponseEntity<CompetitorDTO> updateCompetitor(@PathVariable Integer competitorId, @RequestBody @Valid CompetitorDTO competitor) {
        competitor.setId(competitorId);
        return ResponseEntity.ok(competitorService.update(competitor));
    }

    @DeleteMapping(value = "/{competitorId}")
    ResponseEntity<Object> deleteCompetitor(@PathVariable Integer competitorId) {
        competitorService.delete(competitorId);
        return ResponseEntity.ok().build();
    }
}
