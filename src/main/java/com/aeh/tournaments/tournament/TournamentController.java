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
    ResponseEntity<TournamentReadDTO> getTournamentById(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId).orElse(null));
    }

    @GetMapping("/{tournamentId}")
    ResponseEntity<Set<DuelDTO>> getDuelsByTournamentId(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getDuels(tournamentId));
    }

    @GetMapping("/{tournamentId}")
    ResponseEntity<CompetitorDTO> getWinnerByTournamentId(@PathVariable long tournamentId) {
        return ResponseEntity.ok(tournamentService.getWinner(tournamentId));
    }

    @PostMapping
    ResponseEntity<TournamentReadDTO> createTournament(@RequestBody TournamentDTO tournament) {
        return ResponseEntity.ok(tournamentService.createTournament(tournament));
    }
}
