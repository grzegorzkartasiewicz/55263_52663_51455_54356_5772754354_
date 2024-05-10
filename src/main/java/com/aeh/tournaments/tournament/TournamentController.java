package com.aeh.tournaments.tournament;

import com.aeh.tournaments.competitors.CompetitorDTO;
import com.aeh.tournaments.duel.DuelDTO;
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
    ResponseEntity<TournamentReadDTO> getTournamentById(@PathVariable(name = "tournamentId") long tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId).orElseThrow());
    }

    @PostMapping
    ResponseEntity<TournamentReadDTO> createTournament(@RequestBody TournamentDTO tournament) {
        return ResponseEntity.ok(tournamentService.createTournament(tournament));
    }

    @PutMapping("/{tournamentId}")
    ResponseEntity<TournamentReadDTO> newRound(@PathVariable long tournamentId, @RequestBody TournamentDTO tournament) {
        return ResponseEntity.ok(tournamentService.newRound(tournamentId, tournament));
    }
}
