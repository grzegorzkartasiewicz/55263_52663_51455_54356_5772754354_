package com.aeh.tournaments.competitors.rest;

import com.aeh.tournaments.competitors.CompetitorService;
import com.aeh.tournaments.competitors.model.Competitor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = CompetitorController.INTERFACE_COMPETITORS)
public class CompetitorController {


    public static final String INTERFACE_COMPETITORS = "/competitors";

    private final CompetitorService competitorService;

    public CompetitorController(CompetitorService competitorService) {
        this.competitorService = competitorService;
    }

    @PostMapping
    public ResponseEntity<Competitor> createCompetitor(@RequestBody @Valid Competitor competitor) {
        return ResponseEntity.ok(competitorService.save(competitor));
    }

    @GetMapping
    public ResponseEntity<List<Competitor>> getAllCompetitors() {
        return ResponseEntity.ok(competitorService.getAllCompetitors());
    }

    @PutMapping(value = "/{competitorId}")
    public ResponseEntity<Competitor> updateCompetitor(@PathVariable Integer competitorId, @RequestBody @Valid Competitor competitor) {
        competitor.setId(competitorId);
        return ResponseEntity.ok(competitorService.update(competitor));
    }

    @DeleteMapping(value = "/{competitorId}")
    public ResponseEntity<Object> deleteCompetitor(@PathVariable Integer competitorId) {
        competitorService.delete(competitorId);
        return ResponseEntity.ok().build();
    }
}
