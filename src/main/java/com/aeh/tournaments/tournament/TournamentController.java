package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.Competitor;
import com.aeh.tournaments.duel.Duel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(value = TournamentController.INTERFACE_TOURNAMENTS)
class TournamentController {

    public static final String INTERFACE_TOURNAMENTS = "/tournaments";

    private final TournamentService tournamentService;

    TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping("/{tournamentId}")
    ResponseEntity<Tournament> getTournamentById(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId).orElse(null));
    }

    @GetMapping("/{tournamentId}")
    ResponseEntity<Set<Duel>> getDuelsByTournamentId(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getDuels(tournamentId));
    }

    @GetMapping("/{tournamentId}")
    ResponseEntity<Competitor> getWinnerByTournamentId(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getWinner(tournamentId));
    }

    @PostMapping
    ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        return ResponseEntity.ok(tournamentService.createTournament(tournament));
    }
}
